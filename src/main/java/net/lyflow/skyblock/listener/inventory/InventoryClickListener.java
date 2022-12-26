package net.lyflow.skyblock.listener.inventory;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.event.island.CreateIslandEvent;
import net.lyflow.skyblock.event.itemshop.PlayerBuyItemEvent;
import net.lyflow.skyblock.event.itemshop.PlayerSellItemEvent;
import net.lyflow.skyblock.inventory.shop.AmountItemShopInventory;
import net.lyflow.skyblock.inventory.shop.ShopCategoryInventory;
import net.lyflow.skyblock.inventory.shop.ShopInventory;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.shop.ShopCategory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;

public class InventoryClickListener implements Listener {

    private final SkyBlock skyBlock;
    public InventoryClickListener(SkyBlock skyblock) {
        this.skyBlock = skyblock;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        final ItemStack item = event.getCurrentItem();

        if(inventory == null || inventory == player.getInventory() || item == null || item.getType() == Material.AIR) return;

        final String title = event.getView().getTitle();

        if(title.equalsIgnoreCase("§6Difficulté de l'île")) {
            event.setCancelled(true);
            player.closeInventory();
            if(item.getType() == Material.STRUCTURE_VOID) {
                //player.closeInventory();
                return;
            }
            //player.closeInventory();
            skyBlock.getServer().getPluginManager()
                    .callEvent(new CreateIslandEvent(skyBlock, player, IslandDifficulty.getIslandDifficultyByMaterial(item.getType())));
            return;
        }

        if(title.equalsIgnoreCase("§9Shop")) {
            event.setCancelled(true);

            switch(item.getType()) {
                case CLOCK -> player.openInventory(ShopInventory.getServeurShopInventory());
                case RECOVERY_COMPASS -> player.openInventory(ShopInventory.getPlayerShopInventory());
            }
            return;
        }

        if(title.equals("§aShop")) {
            event.setCancelled(true);

            switch(item.getType()) {
                case PAPER -> player.openInventory(ShopInventory.getShopMenuInventory());
                case AMETHYST_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(false));
                case ECHO_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(true));
            }

            return;
        }

        if(title.startsWith("§aShop/Category")) {
            event.setCancelled(true);
            player.openInventory((item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§9Back"))
                    ? ShopInventory.getServeurShopInventory()
                    : ShopInventory.getShopServerInventory(0, ShopCategory.getShopCategory(event.getSlot()), title.contains("Buy")));
            return;
        }

        if(title.startsWith("§aShop/Amount")) {
            event.setCancelled(true);

            final int page = Integer.parseInt(title.substring(title.length()-1));
            final boolean isBuyInventory = title.contains("Buy");
            if(item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§9Back")) {
                final ShopCategory shopCategory = ShopCategory.getShopCategoryByInventoryName(title);
                player.openInventory(ShopInventory.getShopServerInventory(page, shopCategory, isBuyInventory));
                return;
            }

            final ItemStack selectedItem = inventory.getItem(4);
            final ItemShop itemShop = ItemShop.getItemShopByMaterial(selectedItem.getType());
            switch(item.getType()) {
                case LIME_STAINED_GLASS_PANE, RED_STAINED_GLASS_PANE -> {
                    try {
                        final String lore = selectedItem.getItemMeta().getLore().get(0);
                        player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(
                                skyBlock, player.getUniqueId(), itemShop, Math.max(Integer.parseInt(lore.substring(lore.lastIndexOf(":")+2)) + Integer.parseInt(
                                        ChatColor.stripColor(item.getItemMeta().getDisplayName())), 0), page, isBuyInventory));
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                case LIGHT_BLUE_STAINED_GLASS_PANE -> {
                    try {
                        final int count = (isBuyInventory)
                                ? (int) Math.floor(new AccountRequest(skyBlock.getDatabase(), true).getMoney(player.getUniqueId()) / itemShop.getBuyPrice())
                                : countItemInventory(player.getInventory(), selectedItem.getType());
                        player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(
                                skyBlock, player.getUniqueId(), itemShop, count , page, isBuyInventory));
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                default -> skyBlock.getServer().getPluginManager().callEvent((isBuyInventory)
                        ? new PlayerBuyItemEvent(skyBlock, player, itemShop, Integer.parseInt(selectedItem.getItemMeta().getLore().get(0)))
                        : new PlayerSellItemEvent(skyBlock, player, itemShop, Integer.parseInt(selectedItem.getItemMeta().getLore().get(0))));
            }

            return;
        }

        if(title.startsWith("§aShop")) {
            event.setCancelled(true);

            final int page = Integer.parseInt(title.substring(title.length()-1));
            final boolean isBuyInventory = title.contains("Buy");
            if(item.getType() == Material.PAPER) {
                final boolean isPrevious = item.getItemMeta().getDisplayName().equals("Previous");
                if(isPrevious || item.getItemMeta().getDisplayName().equals("Next")) {
                    final ShopCategory shopCategory = ShopCategory.getShopCategoryByInventoryName(title);
                    if(isPrevious) {
                        player.openInventory((page == 0) ? ShopCategoryInventory.getShopCategoryInventory(isBuyInventory) :
                                ShopInventory.getShopServerInventory(page-1, shopCategory, isBuyInventory));
                        return;
                    }
                    player.openInventory(ShopInventory.getShopServerInventory(page+1, shopCategory, isBuyInventory));
                    return;
                }
            }
            if(item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;
            try {
                player.openInventory(AmountItemShopInventory.getAmountItemShopInventory(skyBlock, player.getUniqueId(), ItemShop.getItemShopByMaterial(item.getType()), 0, page, isBuyInventory));
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private int countItemInventory(Inventory inventory, Material material) {
        int result = 0;
        for(int i=0; i<inventory.getSize(); i++) {
            final ItemStack itemStack = inventory.getItem(i);
            if(itemStack == null || itemStack.getType() != material) continue;
            result += itemStack.getAmount();
        }
        return result;
    }
}
