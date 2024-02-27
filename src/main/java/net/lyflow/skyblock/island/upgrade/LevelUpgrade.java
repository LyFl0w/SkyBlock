package net.lyflow.skyblock.island.upgrade;

import net.lyflow.skyblock.utils.ArrayUtils;

import java.util.List;
import java.util.Set;

public class LevelUpgrade {

    private final float[] prices;
    private final List<List<String>> descriptions;
    private final int[] slots;

    public LevelUpgrade(float[] prices, int[] slot, List<List<String>> descriptions) {
        if (prices.length != slot.length)
            throw new IllegalArgumentException("There must be as many slots as upgrades");

        this.prices = prices;
        this.slots = slot;
        this.descriptions = descriptions;
    }

    public LevelUpgrade(List<Float> prices, Set<Integer> slots, List<List<String>> descriptions) {
        this(ArrayUtils.toFloatArray(prices), slots.stream().mapToInt(Integer::intValue).toArray(), descriptions);
    }

    public LevelUpgrade(float price, String... description) {
        this(new float[]{price}, new int[]{0}, List.of(List.of(description)));
    }

    public float getPrices(int level) {
        if (level < 1 || level > prices.length)
            throw new IllegalArgumentException("Price for " + level + " doesn't exist !");

        return prices[level - 1];
    }

    public List<String> getDescriptions(int level) {
        if (level < 1 || level > prices.length)
            throw new IllegalArgumentException("Description for " + level + " doesn't exist !");

        return descriptions.get(level - 1);
    }

    public int getSlot(int level) {
        if (level < 1 || level > prices.length)
            throw new IllegalArgumentException("Slot for level " + level + " doesn't exist !");

        return slots[level - 1];
    }

    public int getMaxLevel() {
        return prices.length;
    }

    public boolean isOneLevel() {
        return getMaxLevel() == 1;
    }

    public int getLevelForSlot(int slot) {
        if (slot < 1 || slot > slots.length)
            throw new IllegalArgumentException("Level for " + slot + " doesn't exist !");

        for (int level = 1; level <= getMaxLevel(); level++) {
            if (getSlot(level) == slot) return level;
        }

        throw new IllegalStateException();
    }

}