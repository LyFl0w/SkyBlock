package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.challenge.mod.CraftItemChallenge;
import net.lyflow.skyblock.challenge.mod.block.PlaceBlockChallenge;
import net.lyflow.skyblock.challenge.mod.block.RemoveBlockChallenge;
import net.lyflow.skyblock.challenge.mod.entity.KillEntityChallenge;
import net.lyflow.skyblock.challenge.mod.entity.ReproduceAnimalChallenge;
import net.lyflow.skyblock.challenge.mod.shop.BuyItemChallenge;
import net.lyflow.skyblock.challenge.mod.shop.SellItemChallenge;
import net.lyflow.skyblock.utils.StringUtils;
import org.bukkit.NamespacedKey;
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
                throw new IllegalCallerException(e.getMessage(), e.getCause());
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

    public boolean isValidElement(NamespacedKey element) {
        return isValidElement(element.toString());
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
        KILL_ENTITY("Tuer les entitées suivantes :", KillEntityChallenge.class),
        REPRODUCE_ANIMAL("Reproduire les animaux suivants :", ReproduceAnimalChallenge.class),

        PLACE_BLOCK("Placer les blocs suivants :", PlaceBlockChallenge.class),
        REMOVE_BLOCK("Casser les blocs suivants :", RemoveBlockChallenge.class),

        CRAFT_ITEM("Crafter les objets suivants :", CraftItemChallenge.class),

        BUY_ITEM("Acheter les objets suivants :", BuyItemChallenge.class),
        SELL_ITEM("Vender les objets suivants :", SellItemChallenge.class);

        private final String defaultDescription;
        private final Class<? extends SubChallenge<?>> subChallengeClass;

        Type(String defaultDescription, Class<? extends SubChallenge<?>> subChallengeClass) {
            this.defaultDescription = defaultDescription;
            this.subChallengeClass = subChallengeClass;
        }

        public static Type getTypeByName(String name) {
            for (Type type : values()) {
                if (name.equals(type.name().toLowerCase())) return type;
            }
            throw new IllegalArgumentException("the difficulty " + name + " doesn't exist !");
        }

        public List<String> getDefaultDescription(ChallengeProgressManager challengeProgressManager, Player player) {
            final ArrayList<String> description = new ArrayList<>(Arrays.asList("", "§9§l" + defaultDescription));

            final Map<List<String>, Integer> defaultCounter = challengeProgressManager.getDefaultCounters().get(this);
            final Map<List<String>, Integer> playerCounter = challengeProgressManager.getPlayerChallengeProgress(player).getPlayerCounters().get(this).getCounter();

            final boolean isKeyObjective = playerCounter.keySet().iterator().next().get(0).contains(":");

            playerCounter.forEach((objectives, totalPlayer) -> {
                final StringBuilder line = new StringBuilder();
                final int totalToReach = defaultCounter.get(objectives);
                final String objectifName = (isKeyObjective) ? objectives.get(0).split(":")[1] : objectives.get(0);

                line.append((totalPlayer >= totalToReach) ? "§a§m" : "§c").append("- ").append(totalPlayer).append("/").append(totalToReach)
                        .append(" ").append(StringUtils.capitalizeSentence(objectifName, "_", " "));

                final List<String> nextObjectives = new ArrayList<>(objectives);
                nextObjectives.remove(0);

                nextObjectives.forEach(objective ->
                        line.append(" ou ").append(StringUtils.capitalizeSentence((isKeyObjective) ? objective.split(":")[1] : objective, "_", " ")));

                description.add(line.toString());
            });
            return description;
        }

        public Class<? extends SubChallenge<?>> getSubChallengeClass() {
            return subChallengeClass;
        }
    }

    public <E extends Enum<E>> List<List<E>> convertToEnumLists(List<List<String>> listOfLists, Class<E> enumClass) {
        final List<List<E>> result = new ArrayList<>();
        for (List<String> strings : listOfLists) {
            final List<E> enumValues = new ArrayList<>();
            for (String string : strings) {
                try {
                    E enumValue = Enum.valueOf(enumClass, string.toUpperCase());
                    enumValues.add(enumValue);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(string + " n'est pas un élément valide pour " + enumClass.getSimpleName());
                }
            }
            result.add(enumValues);
        }
        return result;
    }

}
