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

    private void addLevel() {
        currentLevel += 1;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getLastBuyLevel() {
        return lastBuyLevel;
    }

    private void addLastBuyLevel() {
        lastBuyLevel += 1;
    }

    public void addTotalLevel() {
        addLevel();
        addLastBuyLevel();
    }

}
