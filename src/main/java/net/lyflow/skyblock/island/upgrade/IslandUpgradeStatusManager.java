package net.lyflow.skyblock.island.upgrade;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IslandUpgradeStatusManager {

    private final IslandUpgrade islandUpgrade;
    private final Map<Integer, IslandUpgradeStatus> islandsUpgradeStatus;

    public IslandUpgradeStatusManager(IslandUpgrade islandUpgrade) {
        this.islandUpgrade = islandUpgrade;
        this.islandsUpgradeStatus = new HashMap<>();
    }

    public void loadIslandUpgrade(int islandID, @NotNull IslandUpgradeStatus islandUpgradeStatus) {
        if (islandsUpgradeStatus.containsKey(islandID))
            throw new IllegalCallerException("The island id " + islandID + " already has a status for " + islandUpgrade.itemInfo.getName() + " upgrade");

        islandsUpgradeStatus.put(islandID, islandUpgradeStatus);
    }

    public IslandUpgradeStatus getIslandUpgradeStatus(int id) {
        return islandsUpgradeStatus.get(id);
    }

    public boolean hasNextIslandCurrentLevelUpgrade(int id) {
        return getIslandUpgradeStatus(id).getCurrentLevel() < islandUpgrade.levelUpgradeManager.getMaxLevel();
    }

    public boolean hasNextIslandLevelUpgrade(int id) {
        return getIslandUpgradeStatus(id).getLastBuyLevel() < islandUpgrade.levelUpgradeManager.getMaxLevel();
    }

    public boolean hasBeforeIslandCurrentLevelUpgrade(int id) {
        return getIslandUpgradeStatus(id).getCurrentLevel() > 1;
    }

    public IslandUpgrade getIslandUpgrade() {
        return islandUpgrade;
    }

    public Map<Integer, IslandUpgradeStatus> getIslandsUpgradeStatus() {
        return islandsUpgradeStatus;
    }
}
