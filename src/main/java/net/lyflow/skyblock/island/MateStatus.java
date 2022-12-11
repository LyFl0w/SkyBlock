package net.lyflow.skyblock.island;

import java.util.Arrays;

public enum MateStatus {

    OWNER(1), MATE(2);

    private final int id;

    MateStatus(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public static MateStatus getMateStatusByID(int id) {
        return Arrays.stream(values()).filter(mateStatus -> mateStatus.getID() == id).findFirst().get();
    }
}