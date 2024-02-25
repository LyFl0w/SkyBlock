package net.lyflow.skyblock.island;

import java.util.Arrays;
import java.util.Optional;

public enum PlayerIslandStatus {

    OWNER(1), MATE(2);

    private final int id;

    PlayerIslandStatus(int id) {
        this.id = id;
    }

    public static PlayerIslandStatus getMateStatusByID(int id) {
        final Optional<PlayerIslandStatus> optionalPlayerIslandStatus = Arrays.stream(values()).filter(playerIslandStatus -> playerIslandStatus.getID() == id).findFirst();
        if (optionalPlayerIslandStatus.isEmpty())
            throw new IllegalArgumentException("Le status d'id " + id + " n'existe pas !");
        return optionalPlayerIslandStatus.get();
    }

    public int getID() {
        return id;
    }
}