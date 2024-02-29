package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatusManager;
import net.lyflow.skyblock.island.upgrade.LevelUpgradeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.logging.Level;

public class BuyIslandLevelUpgradeEvent extends IslandUpgradeLeveledEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public BuyIslandLevelUpgradeEvent(SkyBlock skyBlock, Player player, IslandUpgrade islandUpgrade, int levelFrom, int levelTo) {
        super(player, islandUpgrade, levelFrom, levelTo);

        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);
        try {
            final int islandID = islandRequest.getIslandID(player.getUniqueId());
            final IslandUpgradeStatusManager islandUpgradeStatusManager = islandUpgrade.getIslandUpgradeStatusManager();
            final IslandUpgradeStatus upgradeStatus = islandUpgradeStatusManager.getIslandUpgradeStatus(islandID);
            final LevelUpgradeManager levelUpgradeManager = islandUpgrade.getLevelUpgradeManager();

            if (!upgradeStatus.isBuy() || levelUpgradeManager.isOneLevel() || levelTo > levelUpgradeManager.getMaxLevel()
                    || levelTo != upgradeStatus.getLastBuyLevel() + 1) {
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            // UPGRADE TO BUY
            final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);

            float playerMoney = accountRequest.getMoney(player.getUniqueId());
            final float price = islandUpgrade.getLevelUpgradeManager().getPrices(upgradeStatus.getCurrentLevel() + 1);
            if (price > playerMoney) {
                player.sendMessage("§cTu n'as pas assez d'argent !");
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            upgradeStatus.setTotalLevel(levelTo);
            accountRequest.setMoney(player.getUniqueId(), playerMoney - price);

            new UpgradeIslandRequest(skyBlock.getDatabase(), false).updateIslandUpgrade(islandID, islandUpgrade.getID(), upgradeStatus);

            player.sendMessage("§bVous avez débloquer le lvl " + upgradeStatus.getCurrentLevel() + " de l'upgrade " + islandUpgrade.getName());
            skyBlock.getDatabase().closeConnection();
        } catch (SQLException e) {
            skyBlock.getLogger().log(Level.SEVERE, e.getMessage(), e);
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
