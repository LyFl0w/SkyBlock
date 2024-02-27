package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class BuyIslandUpgradeEvent extends IslandUpgradeEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public BuyIslandUpgradeEvent(SkyBlock skyBlock, Player player, IslandUpgrade islandUpgrade) {
        super(player, islandUpgrade);
        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

        try {
            final int islandID = islandRequest.getIslandID(player.getUniqueId());
            final IslandUpgradeStatus upgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);

            if (upgradeStatus.isBuy()) {
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);

            float playerMoney = accountRequest.getMoney(player.getUniqueId());
            final float price = islandUpgrade.getLevelUpgrade().getPrices(1);
            if (price > playerMoney) {
                player.sendMessage("§cTu n'as pas assez d'argent !");
                setCancelled(true);

                skyBlock.getDatabase().closeConnection();
                return;
            }

            upgradeStatus.setTotalLevel(1);

            new UpgradeIslandRequest(skyBlock.getDatabase(), false).updateIslandUpgrade(islandID, islandUpgrade.getID(), upgradeStatus);
            accountRequest.setMoney(player.getUniqueId(), playerMoney - price);

            player.sendMessage("§bUpgrade " + islandUpgrade.getName() + " acheté et activé !");
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
