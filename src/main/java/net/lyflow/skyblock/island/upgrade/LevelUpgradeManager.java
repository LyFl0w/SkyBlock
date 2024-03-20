package net.lyflow.skyblock.island.upgrade;

import java.util.List;

public class LevelUpgradeManager {

    final List<? extends LevelUpgrade> levelUpgrades;

    public LevelUpgradeManager(List<? extends LevelUpgrade> levelUpgrades) {
        this.levelUpgrades = levelUpgrades;

        final Class<?> checkClass = levelUpgrades.get(0).getClass();
        for (int i = 1; i < getMaxLevel(); i++) {
            if (!levelUpgrades.get(i).getClass().equals(checkClass))
                throw new IllegalArgumentException("Non-compliance with upgrade rules\nPlease take a look at the documentation on upgrades");
        }
    }

    public LevelUpgrade getLevelUpgrade(int level) {
        if (level < 1 || level > levelUpgrades.size())
            throw new IllegalArgumentException("There is no level " + level);

        return levelUpgrades.get(level - 1);
    }

    public float getPrices(int level) {
        return getLevelUpgrade(level).getPrice();
    }

    public List<String> getDescriptions(int level) {
        return getLevelUpgrade(level).getDescription();
    }

    public int getSlot(int level) {
        return getLevelUpgrade(level).getSlot();
    }

    public int getMaxLevel() {
        return levelUpgrades.size();
    }

    public boolean isOneLevel() {
        return getMaxLevel() == 1;
    }

    public int getLevelForSlot(int slot) {
        if (slot < 1 || slot > levelUpgrades.size())
            throw new IllegalArgumentException("Level for the slot n°" + slot + " doesn't exist !");

        final int maxLevel = getMaxLevel() - 1;
        for (int level = 0; level <= maxLevel; level++) {
            if (levelUpgrades.get(level).getSlot() == slot) return level + 1;
        }

        throw new IllegalStateException();
    }

}
