package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
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

public class BuyIslandLevelUpgradeEvent extends IslandUpgradeLeveledEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public BuyIslandLevelUpgradeEvent(SkyBlock skyBlock, Player player, IslandUpgrade islandUpgrade, int level) {
        super(player, islandUpgrade, level);
        System.out.println("buy upgrade");
        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

        try {
            final int islandID = islandRequest.getIslandID(player.getUniqueId());
            final IslandUpgradeStatusManager islandUpgradeStatusManager = islandUpgrade.getIslandUpgradeStatusManager();
            final IslandUpgradeStatus upgradeStatus = islandUpgradeStatusManager.getIslandUpgradeStatus(islandID);
            final LevelUpgrade levelUpgrade = islandUpgrade.getLevelUpgrade();

            if (!upgradeStatus.isBuy() || levelUpgrade.isOneLevel() || level > levelUpgrade.getMaxLevel()
                    || level != upgradeStatus.getLastBuyLevel() + 1) {
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            // UPGRADE TO BUY
            final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);

            float playerMoney = accountRequest.getMoney(player.getUniqueId());
            final float price = islandUpgrade.getLevelUpgrade().getPrices(upgradeStatus.getCurrentLevel() + 1);
            if (price > playerMoney) {
                player.sendMessage("§cTu n'as pas assez d'argent !");
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            upgradeStatus.setTotalLevel(level);
            accountRequest.setMoney(player.getUniqueId(), playerMoney - price);

            new UpgradeIslandRequest(skyBlock.getDatabase(), false).updateIslandUpgrade(islandID, islandUpgrade.getID(), upgradeStatus);

            player.sendMessage("§bVous avez débloquer le lvl " + upgradeStatus.getCurrentLevel() + " de l'upgrade " + islandUpgrade.getName());
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
