package net.lyflow.skyblock.event.island.upgrade;


import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.upgrade.IslandUpgrade;
import net.lyflow.skyblock.upgrade.IslandUpgradeStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class BuyIslandUpgradeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;

    public BuyIslandUpgradeEvent(SkyBlock skyBlock, Player player, IslandUpgrade islandUpgrade) {
        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

        try {
            final int islandID = islandRequest.getIslandID(player.getUniqueId());
            final IslandUpgradeStatus upgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);

            if (upgradeStatus.isBuy()) {
                setCancelled(true);
                return;
            }

            final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);


            float playerMoney = accountRequest.getMoney(player.getUniqueId());
            if (islandUpgrade.getPrice() > playerMoney) {
                player.sendMessage("§cTu n'as pas assez d'argent !");
                setCancelled(true);
                return;
            }

            upgradeStatus.setBuy(true);
            upgradeStatus.setEnable(true);

            new UpgradeIslandRequest(skyBlock.getDatabase(), false).updateIslandUpgrade(islandID, islandUpgrade.getID(), upgradeStatus);
            accountRequest.setMoney(player.getUniqueId(), playerMoney - islandUpgrade.getPrice());

            player.sendMessage("§bUpgrade acheté et activé !");
            skyBlock.getDatabase().closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override @NotNull
    public HandlerList getHandlers() {
        return getHandlerList();
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean setCancelled) {
        this.isCancelled = setCancelled;
    }
}
