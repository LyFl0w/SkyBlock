package net.lyflow.skyblock.upgrade;

public class IslandUpgradeStatus {

    private boolean isBuy, isEnable;

    public IslandUpgradeStatus(boolean isBuy, boolean isEnable) {
        this.isBuy = isBuy;
        this.isEnable = isEnable;
    }

    public IslandUpgradeStatus() {
        this(false, false);
    }

    public boolean isBuy() {
        return isBuy;
    }

    public void setBuy(boolean buy) {
        isBuy = buy;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public String toString() {
        return "IslandUpgradeStatus{" +
                "isBuy=" + isBuy +
                ", isEnable=" + isEnable +
                '}';
    }
}
