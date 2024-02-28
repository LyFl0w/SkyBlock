package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class SubChallenge<T extends Event> {

    protected Challenge challenge;
    private final Type type;
    private final List<Integer> counterList;
    private final List<List<String>> elementsCounter;

    protected SubChallenge(Type type, List<Integer> counterList, List<List<String>> elementsCounter) {
        this.type = type;
        this.counterList = counterList;
        this.elementsCounter = elementsCounter;
    }

    protected void onEventTriggered(Player player, T event) {
        final PlayerChallenge playerChallengeProgress = challenge.getChallengeProgressManager().getPlayerChallengeProgress(player);
        final ChallengeStatus challengeStatus = playerChallengeProgress.getStatus();

        if (challengeStatus == ChallengeStatus.IN_PROGRESS || (challengeStatus == ChallengeStatus.LOCKED && challenge.isProgressWhenBlocked())) {
            try {
                onEvent(event, player, playerChallengeProgress);
            } catch (SQLException e) {
                throw new IllegalCallerException(e);
            }
        }
    }

    protected abstract void onEvent(T event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException;

    public Type getType() {
        return type;
    }

    public List<List<String>> getElementsCounter() {
        return elementsCounter;
    }

    public List<Integer> getCounterList() {
        return counterList;
    }

    public void initChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

    public Challenge getChallenge() {
        return this.challenge;
    }

    public <V extends Enum<V>> boolean isValidElement(V element) {
        return isValidElement(element.name());
    }

    public boolean isValidElement(String element) {
        return challenge.getChallengeProgressManager().isValidElement(this.type, element);
    }

    public <V extends Enum<V>> void incrementCounter(Player player, int increment, V element) throws SQLException {
        incrementCounter(player, increment, element.name());
    }

    public void incrementCounter(Player player, int increment, String element) throws SQLException {
        challenge.getChallengeProgressManager().incrementCounter(player, this.type, increment, element);
    }

    public enum Type {
        KILL_ENTITY("Tuer les entitées suivantes :"),
        REPRODUCE_ANIMAL("Reproduire les animaux suivants :"),

        PLACE_BLOCK("Placer les blocs suivants :"),
        REMOVE_BLOCK("Casser les blocs suivants :"),

        CRAFT_ITEM("Crafter les objets suivants :"),

        BUY_ITEM("Acheter les objets suivants :"),
        SELL_ITEM("Vender les objets suivants :");

        private final String defaultDescription;

        Type(String defaultDescription) {
            this.defaultDescription = defaultDescription;
        }

        public List<String> getDefaultDescription(ChallengeProgressManager challengeProgressManager, Player player) {
            final ArrayList<String> description = new ArrayList<>(Arrays.asList("", "§9§l" + defaultDescription));

            final Map<List<String>, Integer> defaultCounter = challengeProgressManager.getDefaultCounters().get(this);
            final Map<List<String>, Integer> playerCounter = challengeProgressManager.getPlayerChallengeProgress(player).getPlayerCounters().get(this).getCounter();

            playerCounter.forEach((objectives, totalPlayer) -> {
                final StringBuilder line = new StringBuilder();
                final int totalToReach = defaultCounter.get(objectives);
                line.append((totalPlayer >= totalToReach) ? "§a§m" : "§c").append("- ").append(totalPlayer).append("/").append(totalToReach)
                        .append(" ").append(StringUtils.capitalizeSentence(objectives.get(0), "_", " "));

                final List<String> nextObjectives = new ArrayList<>(objectives);
                nextObjectives.remove(0);

                nextObjectives.forEach(objective -> line.append(" ou ").append(StringUtils.capitalizeSentence(objective, "_", " ")));

                description.add(line.toString());
            });
            return description;
        }
    }
}
