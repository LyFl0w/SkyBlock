package net.lyflow.skyblock.inventory.shop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.shop.ShopCategory;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ShopInventory {

    private ShopInventory() {
        throw new IllegalStateException("Inventory class");
    }

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
        final boolean nextPage = (page + 1) * inventorySize - 9 * (page + 1) < itemShopList.size();
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(inventorySize, "§aShop/" + (isBuyInventory ? "Buy" : "Sell") + "/" + shopCategory.getName() + "/" + page);
        inventoryBuilder.setItem(0, new ItemBuilder(Material.PAPER).setName("Previous").toItemStack());
        if (nextPage) inventoryBuilder.setItem(8, new ItemBuilder(Material.PAPER).setName("Next").toItemStack());

        final ItemStack blankItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("-").toItemStack();
        for (int i = 1; i < 9; i++) if (inventoryBuilder.isEmptySlot(i)) inventoryBuilder.setItem(i, blankItem);

        for (int i = page * inventorySize - 9 * page; i < (nextPage ? (page + 1) * inventorySize - 9 * (page + 1) : itemShopList.size()); i++)
            inventoryBuilder.addItem((isBuyInventory) ? itemShopList.get(i).getBuyItemStack(1) : itemShopList.get(i).getSellItemStack(1));

        return inventoryBuilder.toInventory();
    }

    public static void inventoryEvent(InventoryClickEvent event, Player player, ItemStack item) {
        event.setCancelled(true);

        switch (item.getType()) {
            case PAPER -> player.openInventory(ShopInventory.getShopMenuInventory());
            case AMETHYST_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(false));
            case ECHO_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(true));
        }
    }

    public static void inventorySwitchPageEvent(SkyBlock skyBlock, InventoryClickEvent event, String title, Player player, ItemStack item) {
        event.setCancelled(true);

        final int page = Integer.parseInt(title.substring(title.length() - 1));
        final boolean isBuyInventory = title.contains("Buy");
        if (item.getType() == Material.PAPER) {
            final boolean isPrevious = item.getItemMeta().getDisplayName().equals("Previous");
            if (isPrevious || item.getItemMeta().getDisplayName().equals("Next")) {
                final ShopCategory shopCategory = ShopCategory.getShopCategoryByInventoryName(title);
                if (isPrevious) {
                    player.openInventory((page == 0) ? ShopCategoryInventory.getShopCategoryInventory(isBuyInventory) :
                            ShopInventory.getShopServerInventory(page - 1, shopCategory, isBuyInventory));
                    return;
                }
                player.openInventory(ShopInventory.getShopServerInventory(page + 1, shopCategory, isBuyInventory));
                return;
            }
        }
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
        try {
            player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(skyBlock, player.getUniqueId(), ItemShop.getItemShopByMaterial(item.getType()), 0, page, isBuyInventory));
        } catch (SQLException e) {
            throw new IllegalCallerException(e);
        }
    }
}
