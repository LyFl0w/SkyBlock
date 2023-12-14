package net.quillcraft.skyblock.challenge;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.manager.ChallengeManager;
import net.quillcraft.skyblock.utils.StringUtils;
import net.quillcraft.skyblock.utils.builder.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Challenge<T extends Event> {

    protected final SkyBlock skyblock;
    protected final ChallengeProgress challengeProgress;
    private final int id;
    private final int slot;
    private final Material material;
    private final String name;
    private final String[] description;
    private final Difficulty difficulty;
    private final Type type;
    private final List<Integer> lockedChallengesID;
    private final Reward reward;
    private boolean progressWhenBlocked;

    public Challenge(SkyBlock skyblock, int id, Difficulty difficulty, Type type, List<Integer> lockedChallengesID, List<Integer> counterList, List<List<String>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        if (lockedChallengesID.contains(id))
            throw new RuntimeException("the Challenge \"" + name + "\" can't block itself");
        this.skyblock = skyblock;

        this.id = id;

        this.slot = slot;
        this.material = material;
        this.name = name;
        this.description = description;

        this.difficulty = difficulty;
        this.type = type;
        this.lockedChallengesID = lockedChallengesID;

        this.reward = reward;

        this.progressWhenBlocked = false;
        this.challengeProgress = new ChallengeProgress(this, counterList, elementsCounter);
    }

    protected void onEventTriggered(Player player, T event) {
        final PlayerChallengeProgress playerChallengeProgress = challengeProgress.getPlayerChallengeProgress(player);
        final ChallengeStatus challengeStatus = playerChallengeProgress.getStatus();

        if (challengeStatus == ChallengeStatus.IN_PROGRESS || (challengeStatus == ChallengeStatus.LOCKED && progressWhenBlocked)) {
            try {
                onEvent(event, player, playerChallengeProgress);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    protected abstract void onEvent(T event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException;

    public final ItemStack getRepresentation(Player player) {
        final ChallengeStatus status = challengeProgress.getPlayerChallengeProgress(player).getStatus();
        final ItemBuilder itemBuilder = new ItemBuilder(material);

        if (status == ChallengeStatus.LOCKED) {
            itemBuilder.setName(getCensoredName());
            if (getKeyChallenges().size() > 0)
                itemBuilder.setLore(getKeyChallenges().stream()
                        .filter(challenge -> !challenge.getChallengeProgress().getPlayerChallengeProgress(player).getStatus().isFinish())
                        .map(challenge -> "§c- " + ChatColor.stripColor(challenge.getName(player))).toList());
        } else {
            if (status == ChallengeStatus.SUCCESSFUL) {
                itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemBuilder.addEnchant(Enchantment.DAMAGE_ALL, 1);
            }
            final List<String> lore = new ArrayList<>(Arrays.stream(StringUtils.prefixWords("§7", description)).toList());
            lore.addAll(type.getDefaultDescription(reward, challengeProgress, player));
            itemBuilder.setName("§6" + name).setLore(lore);
        }
        return itemBuilder.toItemStack();
    }

    public int getID() {
        return id;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Type getType() {
        return type;
    }

    public ChallengeProgress getChallengeProgress() {
        return challengeProgress;
    }

    public String getName() {
        return name;
    }

    public String getName(Player player) {
        return (getChallengeProgress().getPlayerChallengeProgress(player).getStatus() == ChallengeStatus.LOCKED) ? getCensoredName() : name;
    }

    public String getCensoredName() {
        return "§c????";
    }

    public String[] getDescription() {
        return description;
    }

    public Reward getReward() {
        return reward;
    }

    public int getSlot() {
        return slot;
    }

    public Challenge<? extends Event> toggleProgressWhenBlocked() {
        progressWhenBlocked = true;
        return this;
    }

    public boolean isProgressWhenBlocked() {
        return progressWhenBlocked;
    }

    public boolean isChallengeLocker() {
        return !lockedChallengesID.isEmpty();
    }

    public List<Integer> getLockedChallengesID() {
        return lockedChallengesID;
    }

    public List<Challenge<? extends Event>> getChallengesLocker() {
        return skyblock.getChallengeManager().getRegisteredChallenges().stream().parallel()
                .filter(challenge -> lockedChallengesID.contains(challenge.getID())).toList();
    }

    public List<Challenge<? extends Event>> getKeyChallenges() {
        return skyblock.getChallengeManager().getRegisteredChallenges().stream().parallel()
                .filter(challenge -> challenge.getLockedChallengesID().contains(id)).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Challenge<?> challenge)) return false;
        return id == challenge.id && material == challenge.material && name.equals(challenge.name) && Arrays.equals(description, challenge.description) && difficulty == challenge.difficulty && type == challenge.type && reward.equals(challenge.reward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, material, name, difficulty, type, reward) * 31 + Arrays.hashCode(description);
    }

    public enum Difficulty {
        EASY(2, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE), "§aFacile"),
        NORMAL(3, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "§3Normal"),
        MEDIUM(4, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE), "§9Moyen"),
        HARD(5, new ItemBuilder(Material.RED_STAINED_GLASS_PANE), "§cDifficile"),
        EXTREME(6, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE), "§5Extreme");

        private final ItemBuilder itemBuilder;
        private final String name;
        private final int slot;

        Difficulty(int slot, ItemBuilder itemBuilder, String name) {
            this.itemBuilder = itemBuilder;
            this.name = name;
            this.slot = slot;
        }

        public static Difficulty getChallengeBySlot(int slot) {
            return Arrays.stream(values()).filter(difficulty1 -> difficulty1.getSlot() == slot).findFirst().get();
        }

        public ItemStack getItemStack() {
            return itemBuilder.setName(name).toItemStack();
        }

        public int getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public boolean playerHasAccess(ChallengeManager challengeManager, Player player) {
            if (this == EASY) return true;
            try {
                final List<? extends Challenge<? extends Event>> challenges = challengeManager.getChallengesByDifficulty(getBefore());

                final long totalChallengesFinished = challenges.stream().parallel().filter(challenge -> {
                    final ChallengeStatus playerChallengeStatus = challenge.getChallengeProgress().getPlayerChallengeProgress(player).getStatus();
                    return playerChallengeStatus == ChallengeStatus.SUCCESSFUL || playerChallengeStatus == ChallengeStatus.REWARD_RECOVERED;
                }).count();
                return totalChallengesFinished >= challenges.size() / 2d;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Difficulty getBefore() throws Exception {
            if (this == EASY) throw new Exception("There is no challenge before EASY");
            return getChallengeBySlot(getSlot() - 1);
        }

        public Difficulty getNext() throws Exception {
            if (this == EXTREME) throw new Exception("There is no challenge after EXTREME");
            return getChallengeBySlot(getSlot() + 1);
        }
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

        public ArrayList<String> getDefaultDescription(Reward reward, ChallengeProgress challengeProgress, Player player) {
            final ArrayList<String> description = new ArrayList<>(Arrays.asList("", "§9§l" + defaultDescription));

            challengeProgress.getPlayerChallengeProgress(player).getPlayerCounter().forEach((objectives, totalPlayer) -> {
                final StringBuilder line = new StringBuilder();
                final int totalToReach = challengeProgress.getCounter().get(objectives);
                line.append((totalPlayer >= totalToReach) ? "§a§m" : "§c").append("- ").append(totalPlayer).append("/").append(totalToReach)
                        .append(" ").append(StringUtils.capitalizeSentence(objectives.get(0), "_", " "));

                final List<String> nextObjectives = new ArrayList<>(objectives);
                nextObjectives.remove(0);

                nextObjectives.forEach(objective -> line.append(" ou ").append(StringUtils.capitalizeSentence(objective, "_", " ")));

                description.add(line.toString());
            });

            description.addAll(Arrays.asList("", "§9§lRécompense :"));
            if (!reward.isEmpty()) {
                if (reward.getMoney() > 0) description.add("§a- " + reward.getMoney() + " $");
                if (reward.getLevel() > 0)
                    description.add("§a- " + reward.getLevel() + " niveau" + (reward.getLevel() > 1 ? "x" : ""));
                reward.getItemStacksAward().forEach(itemStack -> description.add("§a- " + itemStack.getAmount() + " " + StringUtils.capitalizeSentence(itemStack
                        .getType().name(), "_", " ")));
            } else {
                description.add("§a- Rien");
            }

            return description;
        }
    }
}
