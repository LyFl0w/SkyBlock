package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.database.request.challenge.ChallengeRequest;

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
    private final ChallengeStatus defaultChallengeStatus;
    private final HashMap<List<String>, Integer> counter;
    private final HashMap<UUID, PlayerChallengeProgress> playersCounter;

    public ChallengeProgress(Challenge<? extends Event> challenge, List<Integer> counterList, List<List<String>> elementsCounter) {
        this.challenge = challenge;
        this.defaultChallengeStatus = (challenge.getDifficulty() == Challenge.Difficulty.EASY) ? ChallengeStatus.IN_PROGRESS : ChallengeStatus.LOCKED;
        this.counter = new HashMap<>(IntStream.range(0, counterList.size()).boxed().collect(Collectors.toUnmodifiableMap(elementsCounter::get, counterList::get)));
        counter.forEach((ts, integer) -> System.out.println("to get ("+challenge.getName()+") : "+ts+" / "+integer));
        this.playersCounter = new HashMap<>();
    }

    public <T extends Enum<T>> void incrementCounter(Player player, int increment, T t) throws SQLException {
        incrementCounter(player, increment, t.name());
    }

    public void incrementCounter(Player player, int increment, String t) throws SQLException {
        final PlayerChallengeProgress playerChallengeProgress = getPlayerChallengeProgress(player);
        if(playerChallengeProgress.getStatus() == ChallengeStatus.SUCCESSFUL || playerChallengeProgress.getStatus() == ChallengeStatus.REWARD_RECOVERED) return;

        final HashMap<List<String>, Integer> playerCounter = playerChallengeProgress.getPlayerCounter();
        final HashMap<List<String>, Integer> playerCounterClone = new HashMap<>(playerCounter);

        playerCounter.entrySet().stream().parallel().filter(entry -> entry.getKey().contains(t)).filter(entry -> counter.get(entry.getKey()) > entry.getValue())
                .forEach(entry -> playerCounter.replace(entry.getKey(), entry.getValue()+increment));

        if(playerCounter.equals(playerCounterClone)) return;

        if(hasCompletedChallenge(player)) accomplished(player);

        final ChallengeRequest challengeRequest = new ChallengeRequest(challenge.skyblock.getDatabase(), true);
        challengeRequest.updateChallenge(challenge.getID(), player.getUniqueId(), playerChallengeProgress);
    }

    public final void accessReward(Player player) {
        final PlayerChallengeProgress playerChallengeProgress = getPlayerChallengeProgress(player);
        if(playerChallengeProgress.getStatus() != ChallengeStatus.SUCCESSFUL) throw new RuntimeException("complete the challenge before validating it");
        playerChallengeProgress.setStatus(ChallengeStatus.REWARD_RECOVERED);

        challenge.getReward().getAward(player);
    }

    public final void accomplished(Player player) {
        player.sendMessage("accomplished");
        final PlayerChallengeProgress playerChallengeProgress = getPlayerChallengeProgress(player);
        if(playerChallengeProgress.getStatus() != ChallengeStatus.IN_PROGRESS) throw new RuntimeException("the challenge is not in progress");
        playerChallengeProgress.setStatus(ChallengeStatus.SUCCESSFUL);

        player.sendMessage("§bVous avez accompli le défi §6"+challenge.getName());
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT, 1, 1);
    }

    public PlayerChallengeProgress initPlayerChallenge(Player player) {
        System.out.println("init player challenge");
        if(playersCounter.containsKey(player.getUniqueId())) throw new RuntimeException("The player "+player.getName()+" already has a counter for the Challenge ("+playersCounter.get(player.getUniqueId())+") size : "+playersCounter.size());

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

}
