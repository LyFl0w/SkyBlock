package net.lyflow.skyblock.inventory.shop;

import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.shop.ShopCategory;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class ShopInventory{

    public static Inventory getShopMenuInventory() {
        return new InventoryBuilder(9, "§9Shop")
                .setItem(2, new ItemBuilder(Material.CLOCK).setName("§6Serveur Shop").toItemStack())
                .setItem(6, new ItemBuilder(Material.RECOVERY_COMPASS).setName("§aPlayer Shop").toItemStack())
                .toInventory();
    }

    public static Inventory getServeurShopInventory() {
        return new InventoryBuilder(9, "§aShop")
                .setItem(0, new ItemBuilder(Material.PAPER).setName("§9Back").toItemStack())
                .setItem(3, new ItemBuilder(Material.ECHO_SHARD).setName("§9Buy").toItemStack())
                .setItem(5, new ItemBuilder(Material.AMETHYST_SHARD).setName("§dSell").toItemStack())
                .toInventory();
    }

    public static Inventory getPlayerShopInventory() {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§bShop");

        return inventoryBuilder.toInventory();
    }

    public static Inventory getShopServerInventory(int page, ShopCategory shopCategory, boolean isBuyInventory) {
        final List<ItemShop> itemShopList = Arrays.stream(ItemShop.values()).filter(itemShop -> itemShop.getShopCategory() == shopCategory)
                .filter((isBuyInventory) ? ItemShop::isPurchasable : ItemShop::isSaleable).toList();
        final int inventorySize = 54;
        final boolean nextPage = (page+1)*inventorySize - 9*(page+1) < itemShopList.size();
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(inventorySize, "§aShop/"+(isBuyInventory ? "Buy" : "Sell")+"/"+shopCategory.getName()+"/"+page);
        inventoryBuilder.setItem(0, new ItemBuilder(Material.PAPER).setName("Previous").toItemStack());
        if(nextPage) inventoryBuilder.setItem(8, new ItemBuilder(Material.PAPER).setName("Next").toItemStack());

        final ItemStack blankItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("-").toItemStack();
        for(int i=1; i<9; i++) if(inventoryBuilder.isEmptySlot(i)) inventoryBuilder.setItem(i, blankItem);

        for(int i=page*inventorySize - 9*page; i<(nextPage ? (page+1)*inventorySize - 9*(page+1) : itemShopList.size()); i++)
            inventoryBuilder.addItem((isBuyInventory) ? itemShopList.get(i).getBuyItemStack(1) : itemShopList.get(i).getSellItemStack(1));

        return inventoryBuilder.toInventory();
    }

}
