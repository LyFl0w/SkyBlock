package net.lyflow.skyblock.inventory.shop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AmountItemShopInventory {

    public static Inventory getAmountItemShopInventory(SkyBlock skyblock, UUID playerUUID, ItemShop itemShop, int amount, int page, boolean isBuyInventory) throws SQLException {
        final ArrayList<String> lore = new ArrayList<>(List.of("§rtotals : " + amount));
        if (amount > 0) {
            final float price = amount * (isBuyInventory ? itemShop.getBuyPrice() : itemShop.getSellPrice());
            lore.add("§2prix total : " + price);
            if (isBuyInventory) {
                final float money = new AccountRequest(skyblock.getDatabase(), true).getMoney(playerUUID);
                if (money < price) lore.set(0, "§c" + ChatColor.stripColor(lore.get(0)));
                lore.add("§aVous avez " + money + "$");
            }
        }

        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§aShop/Amount/" + (isBuyInventory ? "Buy" : "Sell") + "/" + itemShop.getShopCategory().getName() + "/" + page)
                .setItem(4, new ItemBuilder(itemShop.getMaterial(), (amount == 0) ? 1 : amount).setLore(lore).toItemStack())
                .setItem(0, new ItemBuilder(Material.PAPER).setName("§9Back").toItemStack());

        inventoryBuilder.setItem(8, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName("All").toItemStack());

        final int[] choice = {-32, -5, -1, 0, 1, 5, 32};
        for (int i = 1; i < 4; i++)
            if (inventoryBuilder.isEmptySlot(i)) inventoryBuilder.setItem(i, new ItemBuilder(
                    Material.RED_STAINED_GLASS_PANE).setName(("§c") + choice[i - 1]).toItemStack());

        for (int i = 5; i < 8; i++)
            if (inventoryBuilder.isEmptySlot(i)) inventoryBuilder.setItem(i, new ItemBuilder(
                    Material.LIME_STAINED_GLASS_PANE).setName(("§a+") + choice[i - 1]).toItemStack());

        return inventoryBuilder.toInventory();
    }

}
