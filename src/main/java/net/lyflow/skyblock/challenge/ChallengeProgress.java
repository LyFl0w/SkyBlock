package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.challenge.ChallengeRequest;

import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChallengeProgress {

    private final Challenge<? extends Event> challenge;
    private final HashMap<List<String>, Integer> counter;
    private final HashMap<UUID, PlayerChallengeProgress> playersCounter;

    private ChallengeStatus defaultChallengeStatus;

    public ChallengeProgress(Challenge<? extends Event> challenge, List<Integer> counterList, List<List<String>> elementsCounter) {
        this.challenge = challenge;
        this.counter = new HashMap<>(IntStream.range(0, counterList.size()).boxed().collect(Collectors.toUnmodifiableMap(elementsCounter::get, counterList::get)));
        this.playersCounter = new HashMap<>();
    }

    public <T extends Enum<T>> void incrementCounter(Player player, int increment, T t) throws SQLException {
        incrementCounter(player, increment, t.name());
    }

    public void incrementCounter(Player player, int increment, String t) throws SQLException {
        final PlayerChallengeProgress playerChallengeProgress = getPlayerChallengeProgress(player);
        final ChallengeStatus challengeStatus = playerChallengeProgress.getStatus();
        if(challengeStatus == ChallengeStatus.SUCCESSFUL || challengeStatus == ChallengeStatus.REWARD_RECOVERED) return;

        final HashMap<List<String>, Integer> playerCounter = playerChallengeProgress.getPlayerCounter();
        final HashMap<List<String>, Integer> playerCounterClone = new HashMap<>(playerCounter);

        playerCounter.entrySet().stream().parallel().filter(entry -> entry.getKey().contains(t)).filter(entry -> counter.get(entry.getKey()) > entry.getValue())
                .forEach(entry -> playerCounter.replace(entry.getKey(), entry.getValue()+increment));

        if(playerCounter.equals(playerCounterClone)) return;

        if(hasCompletedChallenge(player) && challengeStatus == ChallengeStatus.IN_PROGRESS) accomplished(player);

        final ChallengeRequest challengeRequest = new ChallengeRequest(challenge.skyblock.getDatabase(), true);
        challengeRequest.updateChallenge(challenge.getID(), player.getUniqueId(), playerChallengeProgress);
    }

    public final void accessReward(Player player) {
        final PlayerChallengeProgress playerChallengeProgress = getPlayerChallengeProgress(player);
        if(playerChallengeProgress.getStatus() != ChallengeStatus.SUCCESSFUL) throw new RuntimeException("complete the challenge before validating it");
        playerChallengeProgress.setStatus(ChallengeStatus.REWARD_RECOVERED);

        try {
            new ChallengeRequest(challenge.skyblock.getDatabase(), true).updateChallenge(challenge.getID(), player.getUniqueId(), playerChallengeProgress);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

        challenge.getReward().getAward(player);
    }

    public final void accomplished(Player player) {
        try {
            final ChallengeManager challengeManager = challenge.skyblock.getChallengeManager();
            final Challenge.Difficulty nextDifficulty = challenge.getDifficulty().getNext();
            final boolean hasAccessToNextPage = nextDifficulty.playerHasAccess(challengeManager, player);

            final PlayerChallengeProgress playerChallengeProgress = getPlayerChallengeProgress(player);
            if(playerChallengeProgress.getStatus() != ChallengeStatus.IN_PROGRESS) throw new RuntimeException("the challenge is not in progress");
            playerChallengeProgress.setStatus(ChallengeStatus.SUCCESSFUL);

            player.sendMessage("§bVous avez accompli le défi §6"+challenge.getName());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);

            final Database database = challenge.skyblock.getDatabase();
            challenge.getLockedChallengesID().stream().map(challengeManager::getChallengeByID).filter(nextChallenge ->
                    nextChallenge.getChallengeProgress().canUnlockChallenge(player)).forEach(challengeToUnlock -> {
                        final PlayerChallengeProgress playerNextChallengeProgress = challengeToUnlock.getChallengeProgress().getPlayerChallengeProgress(player);
                        playerNextChallengeProgress.setStatus(ChallengeStatus.IN_PROGRESS);
                        try {
                            new ChallengeRequest(database, false).updateChallenge(challengeToUnlock.getID(), player.getUniqueId(), playerNextChallengeProgress);
                            player.sendMessage("§bVous avez débloqué le challenge "+challengeToUnlock.getName());
                        } catch(SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

            // UNLOCK NEW PAGE
            if(!hasAccessToNextPage && nextDifficulty.playerHasAccess(challengeManager, player)) {
                challengeManager.getChallengesByDifficulty(nextDifficulty).stream().parallel().filter(newChallenge ->
                        newChallenge.getKeyChallenges().size() == 0).forEach(newChallenge -> {
                            final PlayerChallengeProgress playerNewChallengeProgress = newChallenge.getChallengeProgress().getPlayerChallengeProgress(player);
                            playerNewChallengeProgress.setStatus(ChallengeStatus.IN_PROGRESS);
                            newChallenge.getChallengeProgress().getPlayerChallengeProgress(player).setStatus(ChallengeStatus.IN_PROGRESS);
                            try {
                                new ChallengeRequest(database, false).updateChallenge(newChallenge.getID(), player.getUniqueId(), playerNewChallengeProgress);
                            } catch(SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                player.sendMessage("§bVous avez débloqué la page des challenges "+nextDifficulty.getName());
            }

            database.closeConnection();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerChallengeProgress initPlayerChallenge(Player player) {
        System.out.println("init player challenge");
        if(playersCounter.containsKey(player.getUniqueId()))
            throw new RuntimeException("The player "+player.getName()+" already has a counter for the Challenge ("+playersCounter.get(player.getUniqueId())+") size : "+playersCounter.size());

        final HashMap<List<String>, Integer> playerCounter = new HashMap<>(counter.entrySet().stream().parallel().collect(Collectors.toMap(Map.Entry::getKey, r -> 0)));
        final PlayerChallengeProgress result = new PlayerChallengeProgress(playerCounter, defaultChallengeStatus);
        playersCounter.put(player.getUniqueId(), result);
        return result;
    }

    public void loadPlayerChallenge(UUID playerUUID, PlayerChallengeProgress playerProgress) {
        playersCounter.put(playerUUID, playerProgress);
    }

    private boolean hasCompletedChallenge(Player player) {
        return getPlayerChallengeProgress(player).getPlayerCounter().equals(counter);
    }

    public PlayerChallengeProgress getPlayerChallengeProgress(Player player) {
        if(!playersCounter.containsKey(player.getUniqueId())) throw new RuntimeException("The player "+player.getName()+" doesn't have a counter for the Challenge");
        return playersCounter.get(player.getUniqueId());
    }

    public boolean canUnlockChallenge(Player player) {
        if(getPlayerChallengeProgress(player).getStatus() != ChallengeStatus.LOCKED) throw new RuntimeException("The Challenge "+challenge.getName()+" is already unlocked");
        return challenge.getKeyChallenges().stream().parallel().filter(keyChallenge ->
                !keyChallenge.getChallengeProgress().getPlayerChallengeProgress(player).getStatus().isFinish()).count() == 0;
    }

    public <T extends Enum<T>> boolean isValidElement(T element) {
        return counter.keySet().stream().parallel().anyMatch(ts -> ts.contains(element.name()));
    }

    public boolean isValidElement(String element) {
        return counter.keySet().stream().parallel().anyMatch(ts -> ts.contains(element));
    }

    public Map<List<String>, Integer> getCounter() {
        return counter;
    }

    public HashMap<UUID, PlayerChallengeProgress> getPlayersCounter() {
        return playersCounter;
    }

    public void updateDefaultChallengeStatus() {
        this.defaultChallengeStatus = (challenge.getKeyChallenges().size() > 0 || challenge.getDifficulty() != Challenge.Difficulty.EASY) ? ChallengeStatus.LOCKED : ChallengeStatus.IN_PROGRESS;
    }

}
