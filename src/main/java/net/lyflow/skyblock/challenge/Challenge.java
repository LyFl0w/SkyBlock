package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.utils.StringUtils;
import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Challenge<T extends Event> {

    protected final SkyBlock skyblock;

    private final int id;

    private final int slot;
    private final Material material;
    private final String name;
    private final String[] description;

    private final Difficulty difficulty;
    private final Type type;

    private final Reward reward;

    protected final ChallengeProgress challengeProgress;

    public Challenge(SkyBlock skyblock, int id, Difficulty difficulty, Type type, List<Integer> counterList, List<List<String>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        this.skyblock = skyblock;

        this.id = id;

        this.slot = slot;
        this.material = material;
        this.name = name;
        this.description = description;

        this.difficulty = difficulty;
        this.type = type;

        this.reward = reward;

        this.challengeProgress = new ChallengeProgress(this, counterList, elementsCounter);
    }

    protected void onEventTriggered(Player player, T event) {
        skyblock.getServer().getScheduler().runTask(skyblock, () -> {
            final PlayerChallengeProgress playerChallengeProgress = challengeProgress.getPlayerChallengeProgress(player);
            final ChallengeStatus challengeStatus = playerChallengeProgress.getStatus();
            if(challengeStatus == ChallengeStatus.IN_PROGRESS || challengeStatus == ChallengeStatus.LOCKED) {
                try {
                    onEvent(event, player, playerChallengeProgress);
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected abstract void onEvent(T event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException;

    public final ItemStack getRepresentation(Player player) {
        final ChallengeStatus status = challengeProgress.getPlayerChallengeProgress(player).getStatus();
        final ItemBuilder itemBuilder = new ItemBuilder(material);

        if(status == ChallengeStatus.LOCKED) {
            itemBuilder.setName("§c[Locked] "+name).setLore(StringUtils.prefixWords("§c", description));
        } else {
            if(status == ChallengeStatus.SUCCESSFUL) {
                itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemBuilder.addEnchant(Enchantment.DAMAGE_ALL, 1);
            }
            itemBuilder.setName(name).setLore(description);
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

    public String[] getDescription() {
        return description;
    }

    public Reward getReward() {
        return reward;
    }

    public int getSlot() {
        return slot;
    }

    public enum Difficulty {
        EASY(2, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE), "§aFacile"),
        NORMAL(3, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "§3Normal"),
        MEDIUM(4, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE), "§9Moyen"),
        HARD(5, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE), "§5Difficile"),
        EXTREME(6, new ItemBuilder(Material.RED_STAINED_GLASS_PANE), "§5Extreme");

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
            if(this == EASY) return true;
            try {
                final List<? extends Challenge<? extends Event>> challenges = challengeManager.getChallengesByDifficulty(getBefore());

                final long totalChallengesFinished = challenges.stream().parallel().filter(challenge -> {
                    final ChallengeStatus playerChallengeStatus = challenge.getChallengeProgress().getPlayerChallengeProgress(player).getStatus();
                    return playerChallengeStatus == ChallengeStatus.SUCCESSFUL || playerChallengeStatus == ChallengeStatus.REWARD_RECOVERED;
                }).count();
                return totalChallengesFinished >= challenges.size() / 2d;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Difficulty getBefore() throws Exception {
            if(this == EASY) throw new Exception("There is no challenge before EASY");
            return getChallengeBySlot(getSlot()-1);
        }
    }

    public enum Type {
        KILL_ENTITY,
        REPRODUCE_ANIMAL,

        PLACE_BLOCK,
        REMOVE_BLOCK,

        CRAFT_ITEM,

        BUY_ITEM,
        SELL_ITEM,
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Challenge<?> challenge)) return false;
        return id == challenge.id && material == challenge.material && name.equals(challenge.name) && Arrays.equals(description, challenge.description) && difficulty == challenge.difficulty && type == challenge.type && reward.equals(challenge.reward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, material, name, difficulty, type, reward)*31+Arrays.hashCode(description);
    }
}
