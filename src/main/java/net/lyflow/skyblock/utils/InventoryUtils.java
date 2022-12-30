package net.lyflow.skyblock.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils {

    public static void removeItems(Player player, Material material, int amount) {
        final PlayerInventory playerInventory = player.getInventory();

        for(int slot=0; slot<playerInventory.getSize(); slot++) {
            final ItemStack itemStack = playerInventory.getItem(slot);
            if(itemStack == null || itemStack.getType() != material) continue;

            amount -= itemStack.getAmount();
            if(amount < 0)  {
                itemStack.setAmount(-amount);
                return;
            }
            playerInventory.remove(itemStack);
            if(amount == 0) return;
        }
    }

    public static int getCraftedItemAmount(CraftingInventory craftingInventory) {
        final int resultAmount = craftingInventory.getResult().getAmount();
        int itemsChecked = 0;
        int possibleCreations = 1;
        for(ItemStack item : craftingInventory.getMatrix()) {
            if(item == null || item.getType() == Material.AIR) continue;
            possibleCreations = (itemsChecked == 0) ? item.getAmount() : Math.min(possibleCreations, item.getAmount());
            itemsChecked++;
        }
        return possibleCreations * resultAmount;
    }

    public static int countItemInventory(Inventory inventory, Material material) {
        int result = 0;
        for(int i=0; i<inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);
            if(itemStack == null || itemStack.getType() != material) continue;
            result += itemStack.getAmount();
        }
        return result;
    }

}
