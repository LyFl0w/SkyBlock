package net.lyflow.skyblock.inventory.shop;

import net.lyflow.skyblock.shop.ShopCategory;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;

public class ShopCategoryInventory {

    public static Inventory getShopCategoryInventory(boolean isBuyInventory) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§aShop/Category/" + (isBuyInventory ? "Buy" : "Sell"))
                .setItem(0, new ItemBuilder(Material.PAPER).setName("§9Back").toItemStack());
        Arrays.stream(ShopCategory.values()).forEach(shopCategory -> inventoryBuilder.setItem(shopCategory.getPos(), shopCategory.getItemStack()));

        return inventoryBuilder.toInventory();
    }

}
