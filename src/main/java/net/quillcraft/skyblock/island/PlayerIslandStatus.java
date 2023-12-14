package net.quillcraft.skyblock.island;

import java.util.Arrays;

public enum PlayerIslandStatus {

    OWNER(1), MATE(2);

    private final int id;

    PlayerIslandStatus(int id) {
        this.id = id;
    }

    public static PlayerIslandStatus getMateStatusByID(int id) {
        return Arrays.stream(values()).filter(playerIslandStatus -> playerIslandStatus.getID() == id).findFirst().get();
    }

    public int getID() {
        return id;
    }
}