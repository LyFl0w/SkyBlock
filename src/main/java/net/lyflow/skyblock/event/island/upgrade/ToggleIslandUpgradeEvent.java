package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class ToggleIslandUpgradeEvent extends IslandUpgradeLeveledEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public ToggleIslandUpgradeEvent(SkyBlock skyBlock, Player player, IslandUpgrade islandUpgrade, int levelFrom, int levelTo) {
        super(player, islandUpgrade, levelFrom, levelTo);

        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);
        try {
            final int islandID = islandRequest.getIslandID(player.getUniqueId());
            final IslandUpgradeStatus upgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);

            final boolean isEnableBeforeChange = upgradeStatus.isEnable();
            this.levelTo = (isEnableBeforeChange) ? 0 : levelTo;

            if (!upgradeStatus.isBuy() || upgradeStatus.getLastBuyLevel() < levelTo || levelTo < 1) {
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            upgradeStatus.setCurrentLevel(this.levelTo);

            new UpgradeIslandRequest(skyBlock.getDatabase(), false).updateIslandUpgrade(islandID, islandUpgrade.getID(), upgradeStatus);

            if (isEnableBeforeChange) {
                player.sendMessage("§cL'Upgrade " + islandUpgrade.getName() + " est maitenant désactivé");
            } else {
                player.sendMessage("§bL'Upgrade " + islandUpgrade.getName() + " est maitenant activé");
            }

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
