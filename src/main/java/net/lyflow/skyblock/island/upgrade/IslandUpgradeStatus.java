package net.lyflow.skyblock.island.upgrade;

public class IslandUpgradeStatus {

    private int currentLevel;
    private int lastBuyLevel;

    public IslandUpgradeStatus(int currentLevel, int lastBuyLevel) {
        this.currentLevel = currentLevel;
        this.lastBuyLevel = lastBuyLevel;
    }

    public IslandUpgradeStatus() {
        this(0, 0);
    }

    public boolean isBuy() {
        return lastBuyLevel > 0;
    }

    public boolean isEnable() {
        return currentLevel > 0;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getLastBuyLevel() {
        return lastBuyLevel;
    }

    public void setLastBuyLevel(int level) {
        lastBuyLevel = level;
    }

    public void setTotalLevel(int currentLevel) {
        setCurrentLevel(currentLevel);
        setLastBuyLevel(currentLevel);
    }

}
