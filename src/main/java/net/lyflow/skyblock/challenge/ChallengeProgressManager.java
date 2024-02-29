package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.challenge.ChallengeRequest;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChallengeProgressManager {

    private final Challenge challenge;
    private final List<SubChallenge<?>> subChallenges;
    private final Map<SubChallenge.Type, Map<List<String>, Integer>> defaultCounters;
    private final Map<UUID, PlayerChallenge> playersCounters;

    private ChallengeStatus defaultChallengeStatus;

    public ChallengeProgressManager(Challenge challenge, List<SubChallenge<?>> subChallenges) {
        this.challenge = challenge;
        this.subChallenges = subChallenges;

        this.defaultCounters = new HashMap<>();
        this.playersCounters = new HashMap<>();

        subChallenges.stream().parallel().forEach(subChallenge -> {
            subChallenge.initChallenge(challenge);
            defaultCounters.put(subChallenge.getType(), IntStream.range(0, subChallenge.getCounterList().size()).boxed()
                    .collect(Collectors.toUnmodifiableMap(subChallenge.getElementsCounter()::get, subChallenge.getCounterList()::get)));
        });
    }

    public void incrementCounter(Player player, SubChallenge.Type type, int increment, String element) throws SQLException {
        final PlayerChallenge playerChallengeProgress = getPlayerChallengeProgress(player);
        final ChallengeStatus challengeStatus = playerChallengeProgress.getStatus();
        if (challengeStatus == ChallengeStatus.SUCCESSFUL || challengeStatus == ChallengeStatus.REWARD_RECOVERED)
            return;

        final Map<List<String>, Integer> playerCounter = playerChallengeProgress.getPlayerCounters().get(type).getCounter();
        final Map<List<String>, Integer> playerCounterClone = new HashMap<>(playerCounter);
        final Map<List<String>, Integer> defaultCounter = this.defaultCounters.get(type);

        playerCounter.entrySet().stream().parallel().filter(entry -> entry.getKey().contains(element)).filter(entry -> defaultCounter.get(entry.getKey()) > entry.getValue())
                .forEach(entry -> playerCounter.replace(entry.getKey(), Math.min(entry.getValue() + increment, defaultCounter.get(entry.getKey()))));

        if (playerCounter.equals(playerCounterClone)) return;

        if (hasCompletedSubChallenge(type, player) && challengeStatus == ChallengeStatus.IN_PROGRESS) {
            accomplishedSubChallenge(type, player);

            if (hasCompletedChallenge(player))
                accomplishedChallenge(player);
        }

        final ChallengeRequest challengeRequest = new ChallengeRequest(challenge.skyblock.getDatabase(), true);
        challengeRequest.updateChallenge(challenge.getID(), player.getUniqueId(), playerChallengeProgress);
    }

    private boolean hasCompletedSubChallenge(SubChallenge.Type type, Player player) {
        return getPlayerChallengeProgress(player).getPlayerCounters().get(type).getCounter().equals(defaultCounters.get(type));
    }

    private boolean hasCompletedChallenge(Player player) {
        return defaultCounters.keySet().stream().parallel().allMatch(type -> hasCompletedSubChallenge(type, player));
    }

    public final void accomplishedChallenge(Player player) {
        try {
            final ChallengeManager challengeManager = challenge.skyblock.getChallengeManager();
            final Challenge.Difficulty nextDifficulty = challenge.getDifficulty().getNext();
            final boolean hasAccessToNextPage = nextDifficulty.playerHasAccess(challengeManager, player);

            final PlayerChallenge playerChallengeProgress = getPlayerChallengeProgress(player);
            if (playerChallengeProgress.getStatus() != ChallengeStatus.IN_PROGRESS)
                throw new IllegalCallerException("the challenge " + challenge.getName() + " is not in progress");
            playerChallengeProgress.setStatus((challenge.getReward().isEmpty()) ? ChallengeStatus.REWARD_RECOVERED : ChallengeStatus.SUCCESSFUL);

            player.sendMessage("§bVous avez complété le défi §6" + challenge.getName());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);

            final Database database = challenge.skyblock.getDatabase();
            challenge.getLockedChallengesID().stream().map(challengeManager::getChallengeByID).filter(challengeToUnlock ->
                    challengeToUnlock.getChallengeProgressManager().canUnlockChallenge(player)).forEach(challengeToUnlock -> {
                final ChallengeProgressManager challengeProgress = challengeToUnlock.getChallengeProgressManager();
                final PlayerChallenge playerNextChallengeProgress = challengeProgress.getPlayerChallengeProgress(player);

                playerNextChallengeProgress.setStatus(ChallengeStatus.IN_PROGRESS);
                player.sendMessage("§bVous avez débloqué le challenge " + challengeToUnlock.getName());
                if (challengeProgress.hasCompletedChallenge(player)) challengeProgress.accomplishedChallenge(player);

                try {
                    new ChallengeRequest(database, false).updateChallenge(challengeToUnlock.getID(), player.getUniqueId(), playerNextChallengeProgress);
                } catch (SQLException e) {
                    throw new IllegalCallerException(e.getMessage(), e.getCause());
                }
            });

            // UNLOCK NEW PAGE
            if (!hasAccessToNextPage && nextDifficulty.playerHasAccess(challengeManager, player)) {
                player.sendMessage("\n§bVous avez débloqué la page des challenges " + nextDifficulty.getName());
                challengeManager.getChallengesByDifficulty(nextDifficulty).stream().parallel().filter(newChallenge ->
                        newChallenge.getKeyChallenges().isEmpty()).forEach(newChallenge -> {
                    final ChallengeProgressManager challengeProgress = newChallenge.getChallengeProgressManager();
                    final PlayerChallenge playerNewChallengeProgress = challengeProgress.getPlayerChallengeProgress(player);

                    playerNewChallengeProgress.setStatus(ChallengeStatus.IN_PROGRESS);
                    player.sendMessage("§bVous avez débloqué le challenge " + newChallenge.getName());
                    if (challengeProgress.hasCompletedChallenge(player))
                        challengeProgress.accomplishedChallenge(player);

                    try {
                        new ChallengeRequest(database, false).updateChallenge(newChallenge.getID(), player.getUniqueId(), playerNewChallengeProgress);
                    } catch (SQLException e) {
                        throw new IllegalCallerException(e.getMessage(), e.getCause());
                    }
                });
            }

            database.closeConnection();
        } catch (Exception e) {
            challenge.skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public final void accessReward(Player player) {
        final PlayerChallenge playerChallengeProgress = getPlayerChallengeProgress(player);
        if (playerChallengeProgress.getStatus() != ChallengeStatus.SUCCESSFUL)
            throw new IllegalArgumentException("complete the challenge before validating it");
        playerChallengeProgress.setStatus(ChallengeStatus.REWARD_RECOVERED);

        try {
            new ChallengeRequest(challenge.skyblock.getDatabase(), true).updateChallenge(challenge.getID(), player.getUniqueId(), playerChallengeProgress);
        } catch (SQLException e) {
            throw new IllegalCallerException(e.getMessage(), e.getCause());
        }

        challenge.getReward().getAward(player);
    }


    public final void accomplishedSubChallenge(SubChallenge.Type type, Player player) {
        final PlayerChallenge playerChallengeProgress = getPlayerChallengeProgress(player);
        if (playerChallengeProgress.getStatus() != ChallengeStatus.IN_PROGRESS)
            throw new IllegalCallerException("the challenge " + challenge.getName() + " is not in progress");

        final PlayerChallenge.Progress progress = playerChallengeProgress.getPlayerCounters().get(type);
        if (progress.getStatus() == SubChallengeStatus.SUCCESSFUL)
            throw new IllegalCallerException("the " + challenge.getName() + " subchallenge is already successful");

        progress.setStatus(SubChallengeStatus.SUCCESSFUL);

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
    }

    public boolean canUnlockChallenge(Player player) {
        if (getPlayerChallengeProgress(player).getStatus() != ChallengeStatus.LOCKED)
            throw new IllegalCallerException("The Challenge " + challenge.getName() + " is already unlocked");
        return challenge.getKeyChallenges().stream().parallel().allMatch(keyChallenge ->
                keyChallenge.getChallengeProgressManager().getPlayerChallengeProgress(player).getStatus().isFinish());
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void updateDefaultChallengeStatus() {
        this.defaultChallengeStatus = (!challenge.getKeyChallenges().isEmpty() || challenge.getDifficulty() != Challenge.Difficulty.EASY)
                ? ChallengeStatus.LOCKED
                : ChallengeStatus.IN_PROGRESS;
    }

    public PlayerChallenge initPlayerChallenge(Player player) {
        System.out.println("init player challenge");
        if (playersCounters.containsKey(player.getUniqueId()))
            throw new IllegalArgumentException("The player " + player.getName() + " already has a counter for the Challenge (" + playersCounters.get(player.getUniqueId()) + ") size : " + playersCounters.size());

        // INIT CHALLENGE PROGRESS TO 0 FOR EVERY SUBCHALLENGE : Map<Type, Map<List<String>, 0>>
        final Map<SubChallenge.Type, PlayerChallenge.Progress> playerCounters = new HashMap<>(defaultCounters.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new PlayerChallenge.Progress(entry.getValue().entrySet().stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, r -> 0)), SubChallengeStatus.IN_PROGRESS))));

        final PlayerChallenge result = new PlayerChallenge(playerCounters, defaultChallengeStatus);
        playersCounters.put(player.getUniqueId(), result);
        return result;
    }

    public void loadPlayerChallenge(UUID playerUUID, PlayerChallenge playerProgress) {
        playersCounters.put(playerUUID, playerProgress);
    }

    public boolean isValidElement(SubChallenge.Type type, String element) {
        return defaultCounters.get(type).keySet().stream().parallel().anyMatch(ts -> ts.contains(element));
    }

    public List<SubChallenge<?>> getSubChallenges() {
        return subChallenges;
    }

    public Map<UUID, PlayerChallenge> getPlayersCounters() {
        return playersCounters;
    }

    public PlayerChallenge getPlayerChallengeProgress(Player player) {
        if (!playersCounters.containsKey(player.getUniqueId()))
            throw new IllegalArgumentException("The player " + player.getName() + " doesn't have a counter for the Challenge");
        return playersCounters.get(player.getUniqueId());
    }

    protected List<SubChallenge<?>> getChallenges() {
        return subChallenges;
    }

    public Map<SubChallenge.Type, Map<List<String>, Integer>> getDefaultCounters() {
        return defaultCounters;
    }
}
