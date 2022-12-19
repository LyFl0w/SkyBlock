package net.lyflow.skyblock.inventory.shop;

import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class AmountItemShopInventory {

    public static Inventory getAmountItemShopInventory(ItemShop itemShop, int amount, int page, boolean isBuyInventory) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§aShop/Amount/"+(isBuyInventory ? "Buy" : "Sell")+"/"+itemShop.getShopCategory().getName()+"/"+page)
                .setItem(4, new ItemBuilder(itemShop.getMaterial(), amount).setLore(String.valueOf(amount)).toItemStack())
                .setItem(0, new ItemBuilder(Material.PAPER).setName("§9Back").toItemStack());

        if(!isBuyInventory) inventoryBuilder.setItem(8, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("All").toItemStack());

        final int[] choice = {-32, -5, -1, 0, 1, 5, 32, 64};
        for(int i=1; i<9; i++) if(inventoryBuilder.isEmptySlot(i)) inventoryBuilder.setItem(i, new ItemBuilder((i<4) ?
                Material.RED_STAINED_GLASS_PANE : Material.LIME_STAINED_GLASS_PANE).setName((i>4 ? "§a+": "§c")+choice[i-1]).toItemStack());

        return inventoryBuilder.toInventory();
    }

}
