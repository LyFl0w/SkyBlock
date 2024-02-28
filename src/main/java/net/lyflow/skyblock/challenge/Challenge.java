package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.utils.StringUtils;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Challenge {

    protected final SkyBlock skyblock;

    private final int id;
    private final int slot;
    private final Material material;
    private final String name;
    private final String[] description;
    private final Difficulty difficulty;
    private final List<Integer> lockedChallengesID;
    private final Reward reward;
    private final ChallengeProgressManager challengeProgressManager;

    private boolean progressWhenBlocked;

    public Challenge(SkyBlock skyblock, int id, Difficulty difficulty, List<SubChallenge<?>> subChallenges, List<Integer> lockedChallengesID, Reward reward, int slot, Material material, String name, String... description) {
        if (lockedChallengesID.contains(id))
            throw new IllegalArgumentException("the Challenge \"" + name + "\" can't block itself");
        this.skyblock = skyblock;

        this.id = id;

        this.slot = slot;
        this.material = material;
        this.name = name;
        this.description = description;

        this.difficulty = difficulty;
        this.lockedChallengesID = lockedChallengesID;

        this.reward = reward;

        this.progressWhenBlocked = false;

        this.challengeProgressManager = new ChallengeProgressManager(this, subChallenges);
    }

    public Challenge(SkyBlock skyblock, int id, Difficulty difficulty, List<SubChallenge<?>> subChallenges, Reward reward, int slot, Material material, String name, String... description) {
        this(skyblock, id, difficulty, subChallenges, Collections.emptyList(), reward, slot, material, name, description);
    }

    public final ItemStack getRepresentation(Player player) {
        final ChallengeStatus status = challengeProgressManager.getPlayerChallengeProgress(player).getStatus();
        ItemBuilder itemBuilder = new ItemBuilder(material);

        if (status == ChallengeStatus.LOCKED) {
            itemBuilder.setName(getCensoredName());
            if (!getKeyChallenges().isEmpty())
                itemBuilder.setLore(getKeyChallenges().stream()
                        .filter(challenge -> !challenge.getChallengeProgressManager().getPlayerChallengeProgress(player).getStatus().isFinish())
                        .map(challenge -> "§c- " + ChatColor.stripColor(challenge.getName(player))).toList());
        } else {
            if (status == ChallengeStatus.SUCCESSFUL) {
                itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemBuilder.addEnchant(Enchantment.DAMAGE_ALL, 1);
            } else if (status == ChallengeStatus.REWARD_RECOVERED) {
                itemBuilder = new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE);
            }
            final List<String> lore = new ArrayList<>(Arrays.stream(StringUtils.prefixWords("§7", description)).toList());
            challengeProgressManager.getDefaultCounters().keySet().forEach(type -> lore.addAll(type.getDefaultDescription(challengeProgressManager, player)));

            lore.addAll(Arrays.asList("", "§9§lRécompense :"));
            if (!reward.isEmpty()) {
                if (reward.getMoney() > 0) lore.add("§a- " + reward.getMoney() + " $");
                if (reward.getLevel() > 0)
                    lore.add("§a- " + reward.getLevel() + " niveau" + (reward.getLevel() > 1 ? "x" : ""));
                reward.getItemStacksAward().forEach(itemStack -> lore.add("§a- " + itemStack.getAmount() + " " + StringUtils.capitalizeSentence(itemStack
                        .getType().name(), "_", " ")));
            } else {
                lore.add("§a- Rien");
            }

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

    public String getName() {
        return name;
    }

    public String getName(Player player) {
        return (getChallengeProgressManager().getPlayerChallengeProgress(player).getStatus() == ChallengeStatus.LOCKED) ? getCensoredName() : name;
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

    public Challenge toggleProgressWhenBlocked() {
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

    public List<Challenge> getChallengesLocker() {
        return skyblock.getChallengeManager().getRegisteredChallenges().stream().parallel()
                .filter(challenge -> lockedChallengesID.contains(challenge.getID())).toList();
    }

    public List<Challenge> getKeyChallenges() {
        return skyblock.getChallengeManager().getRegisteredChallenges().stream().parallel()
                .filter(challenge -> challenge.getLockedChallengesID().contains(id)).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Challenge challenge)) return false;
        return id == challenge.id && slot == challenge.slot && progressWhenBlocked == challenge.progressWhenBlocked && material == challenge.material && Objects.equals(name, challenge.name) && Arrays.equals(description, challenge.description) && difficulty == challenge.difficulty && Objects.equals(lockedChallengesID, challenge.lockedChallengesID) && Objects.equals(reward, challenge.reward) && Objects.equals(challengeProgressManager, challenge.challengeProgressManager);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, slot, material, name, difficulty, lockedChallengesID, reward, progressWhenBlocked, challengeProgressManager);
        result = 31 * result + Arrays.hashCode(description);
        return result;
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
            final Optional<Difficulty> difficultyOptional = Arrays.stream(values()).filter(difficulty1 -> difficulty1.getSlot() == slot).findFirst();
            if (difficultyOptional.isPresent()) return difficultyOptional.get();
            throw new IllegalArgumentException("Slot doesn't contain difficulty item");
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

            final List<Challenge> challenges = challengeManager.getChallengesByDifficulty(getBefore());

            final long totalChallengesFinished = challenges.stream().parallel().filter(challenge -> {
                final ChallengeStatus playerChallengeStatus = challenge.getChallengeProgressManager().getPlayerChallengeProgress(player).getStatus();
                return playerChallengeStatus == ChallengeStatus.SUCCESSFUL || playerChallengeStatus == ChallengeStatus.REWARD_RECOVERED;
            }).count();
            return totalChallengesFinished >= challenges.size() / 2d;
        }

        public Difficulty getBefore() {
            if (this == EASY) throw new IllegalCallerException("There is no challenge before EASY");
            return getChallengeBySlot(getSlot() - 1);
        }

        public Difficulty getNext() {
            if (this == EXTREME) throw new IllegalCallerException("There is no challenge after EXTREME");
            return getChallengeBySlot(getSlot() + 1);
        }
    }

    public ChallengeProgressManager getChallengeProgressManager() {
        return challengeProgressManager;
    }
}
