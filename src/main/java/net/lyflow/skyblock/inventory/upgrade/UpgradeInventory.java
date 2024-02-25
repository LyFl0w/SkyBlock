package net.lyflow.skyblock.inventory.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.event.island.upgrade.BuyIslandUpgradeEvent;
import net.lyflow.skyblock.event.island.upgrade.ToggleIslandUpgradeEvent;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.upgrade.IslandUpgrade;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;

public class UpgradeInventory {

    public static Inventory getUpgradeInventory(IslandUpgradeManager islandUpgradeManager, int islandID) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Upgrade");
        islandUpgradeManager.getIslandUpgrades().stream().parallel().forEach(islandUpgrade ->
                inventoryBuilder.setItem(islandUpgrade.getSlot(), islandUpgrade.getRepresentation(islandID)));
        return inventoryBuilder.toInventory();
    }


    public static void inventoryEvent(SkyBlock skyBlock, InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        try {
            final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), true);
            final int islandID = islandRequest.getIslandID(player.getUniqueId());

            final IslandUpgrade islandUpgrade = SkyBlock.getInstance().getIslandUpgradeManager().getIslandUpgradesBySlot(event.getSlot());

            skyBlock.getServer().getPluginManager().callEvent(
                    (!islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID).isBuy())
                    ? new BuyIslandUpgradeEvent(skyBlock, player, islandUpgrade)
                    : new ToggleIslandUpgradeEvent(skyBlock, player, islandUpgrade));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
