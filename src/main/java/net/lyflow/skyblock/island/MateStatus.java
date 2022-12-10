package net.lyflow.skyblock.island;

public enum MateStatus {

    OWNER(1), MATE(2);

    private final int id;

    MateStatus(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}