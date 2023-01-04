package net.lyflow.skyblock.inventory.upgrade;

import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;

import org.bukkit.inventory.Inventory;

public class UpgradeInventory {

    public static Inventory getUpgradeInventory(IslandUpgradeManager islandUpgradeManager, int islandID) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Upgrade");
        islandUpgradeManager.getIslandUpgrades().stream().parallel().forEach(islandUpgrade ->
                inventoryBuilder.setItem(islandUpgrade.getSlot(), islandUpgrade.getRepresentation(islandID)));
        return inventoryBuilder.toInventory();
    }

}
