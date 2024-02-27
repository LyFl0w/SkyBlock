package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatusManager;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class LevelUpIslandUpgradeEvent extends IslandUpgradeLeveledEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public LevelUpIslandUpgradeEvent(SkyBlock skyBlock, Player player, IslandUpgrade islandUpgrade, int levelFrom, int levelTo) {
        super(player, islandUpgrade, levelFrom, levelTo);
        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

        try {
            final int islandID = islandRequest.getIslandID(player.getUniqueId());
            final IslandUpgradeStatusManager islandUpgradeStatusManager = islandUpgrade.getIslandUpgradeStatusManager();
            final IslandUpgradeStatus upgradeStatus = islandUpgradeStatusManager.getIslandUpgradeStatus(islandID);
            final LevelUpgrade levelUpgrade = islandUpgrade.getLevelUpgrade();

            if (!upgradeStatus.isBuy() || levelUpgrade.isOneLevel() || levelTo > levelUpgrade.getMaxLevel()
                    || levelTo < upgradeStatus.getCurrentLevel()) {
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            upgradeStatus.setCurrentLevel(levelTo);

            new UpgradeIslandRequest(skyBlock.getDatabase(), false).updateIslandUpgrade(islandID, islandUpgrade.getID(), upgradeStatus);

            player.sendMessage("§bL'Upgrade " + islandUpgrade.getName() + " a été mit au lvl " + upgradeStatus.getCurrentLevel() + " !");
            skyBlock.getDatabase().closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
