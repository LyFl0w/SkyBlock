package net.lyflow.skyblock.inventory.shop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.event.island.CreateIslandEvent;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.shop.ShopCategory;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ShopCategoryInventory {

    public static Inventory getShopCategoryInventory(boolean isBuyInventory) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§aShop/Category/"+(isBuyInventory ? "Buy" : "Sell"))
                .setItem(0, new ItemBuilder(Material.PAPER).setName("§9Back").toItemStack());
        Arrays.stream(ShopCategory.values()).forEach(shopCategory -> inventoryBuilder.setItem(shopCategory.getPos(), shopCategory.getItemStack()));
        
        return inventoryBuilder.toInventory();
    }

    public static void inventoryEvent(InventoryClickEvent event, Player player, ItemStack item) {
        event.setCancelled(true);

        switch(item.getType()) {
            case CLOCK -> player.openInventory(ShopInventory.getServeurShopInventory());
            case RECOVERY_COMPASS -> player.openInventory(ShopInventory.getPlayerShopInventory());
        }
    }

    public static void inventoryCategoryEvent(String title, InventoryClickEvent event, Player player, ItemStack item) {
        event.setCancelled(true);
        player.openInventory((item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§9Back"))
                ? ShopInventory.getServeurShopInventory()
                : ShopInventory.getShopServerInventory(0, ShopCategory.getShopCategory(event.getSlot()), title.contains("Buy")));
    }
}
