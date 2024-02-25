package net.lyflow.skyblock.upgrade;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class IslandUpgradeStatusManager {

    private final IslandUpgrade islandUpgrade;
    private final HashMap<Integer, IslandUpgradeStatus> islandsUpgradeStatus;

    public IslandUpgradeStatusManager(IslandUpgrade islandUpgrade) {
        this.islandUpgrade = islandUpgrade;
        this.islandsUpgradeStatus = new HashMap<>();
    }

    public void loadIslandUpgrade(int islandID, @NotNull IslandUpgradeStatus islandUpgradeStatus) {
        if(islandsUpgradeStatus.containsKey(islandID))
            throw new RuntimeException("The island id "+islandID+" already has a status for "+islandUpgrade.getName()+" upgrade");

        islandsUpgradeStatus.put(islandID, islandUpgradeStatus);
        System.out.println("put (" + islandUpgrade.getName() + ") : " + islandID + " / " + islandUpgradeStatus);
    }

    public IslandUpgradeStatus getIslandUpgradeStatus(int id) {
        return islandsUpgradeStatus.get(id);
    }

    public IslandUpgrade getIslandUpgrade() {
        return islandUpgrade;
    }

    public HashMap<Integer, IslandUpgradeStatus> getIslandsUpgradeStatus() {
        return islandsUpgradeStatus;
    }
}
