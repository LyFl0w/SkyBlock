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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Challenge<T extends Event, U> {

    protected final SkyBlock skyBlock;

    private final int id;

    private final Material material;
    private final String name;
    private final String[] description;

    private final Difficulty difficulty;
    private final Type type;

    private final Reward reward;

    protected final ChallengeProgress<U> challengeProgress;

    public Challenge(SkyBlock skyBlock, int id, Difficulty difficulty, Type type, List<Integer> counterList, List<List<U>> elementsCounter, Reward reward, Material material, String name, String... description) {
        this.skyBlock = skyBlock;

        this.id = id;

        this.material = material;
        this.name = name;
        this.description = description;

        this.difficulty = difficulty;
        this.type = type;

        this.reward = reward;

        this.challengeProgress = new ChallengeProgress<U>(this, counterList, elementsCounter);

        final ChallengeManager challengeManager = skyBlock.getChallengeManager();
        if(challengeManager.challengeExist(id)) {
            final Challenge<?, ?> challenge = challengeManager.getChallengeByID(id);
            throw new RuntimeException((this.equals(challenge)) ? "Their is a duplication of Challenge with id "+id : "The Challenge "+name+" can't be initialized because his id is already use by the Challenge "+challenge.getName());
        }
        challengeManager.getRegisteredChallenges().add(this);
    }

    protected void onEventTriggered(Player player, T event) {
        final PlayerChallengeProgress<U> playerChallengeProgress = challengeProgress.getPlayerChallengeProgress(player);
        if(playerChallengeProgress.getStatus() == ChallengeStatus.IN_PROGRESS) onEvent(event, player, playerChallengeProgress);
    }

    protected abstract void onEvent(T event, Player player, PlayerChallengeProgress<U> playerChallengeProgress);

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

    public int getId() {
        return id;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Type getType() {
        return type;
    }

    public ChallengeProgress<U> getChallengeProgress() {
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

    public enum Difficulty {
        EASY(2, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setName("§aFacile")),
        NORMAL(3, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§3Normal")),
        MEDIUM(4, new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE).setName("§9Moyen")),
        HARD(5, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("§5Difficile")),
        EXTREME(6, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName("§5Extreme"));

        private final ItemBuilder itemBuilder;
        private final int slot;

        Difficulty(int slot, ItemBuilder itemBuilder) {
            this.itemBuilder = itemBuilder;
            this.slot = slot;
        }

        public ItemStack getItemStack() {
            return itemBuilder.toItemStack();
        }

        public int getSlot() {
            return slot;
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
        if(!(o instanceof Challenge<?, ?> challenge)) return false;
        return id == challenge.id && material == challenge.material && name.equals(challenge.name) && Arrays.equals(description, challenge.description) && difficulty == challenge.difficulty && type == challenge.type && reward.equals(challenge.reward);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, material, name, difficulty, type, reward)*31+Arrays.hashCode(description);
    }
}
