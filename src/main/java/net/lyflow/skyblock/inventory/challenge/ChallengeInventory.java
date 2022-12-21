package net.lyflow.skyblock.inventory.challenge;

import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import org.bukkit.inventory.Inventory;

public class ChallengeInventory {

    public static Inventory getMenuChallengeInventory() {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§gChallenges");
        return inventoryBuilder.toInventory();
    }

}
