package net.lyflow.skyblock.listener.inventory;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.event.island.CreateIslandEvent;
import net.lyflow.skyblock.inventory.shop.ShopCategoryInventory;
import net.lyflow.skyblock.inventory.shop.ShopInventory;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.shop.ShopCategory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

        if(title.equals("§aShop/Serveur")) {
            event.setCancelled(true);

            switch(item.getType()) {
                case PAPER -> player.openInventory(ShopInventory.getShopMenuInventory());
                case AMETHYST_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(false));
                case ECHO_SHARD -> player.openInventory(ShopCategoryInventory.getShopCategoryInventory(true));
            }

            return;
        }

        if(title.startsWith("§aShop/Serveur/Category")) {
            event.setCancelled(true);
            player.openInventory((item.getType() == Material.PAPER && item.getItemMeta().getDisplayName().equals("§9Back"))
                    ? ShopInventory.getServeurShopInventory()
                    : ShopInventory.getShopServerInventory(0, ShopCategory.getShopCategory(event.getSlot()), title.contains("Buy")));
            return;
        }

        if(title.startsWith("§aShop/Serveur")) {
            event.setCancelled(true);
            if(item.getType() == Material.PAPER) {
                final boolean isPrevious = item.getItemMeta().getDisplayName().equals("Previous");
                if(isPrevious || item.getItemMeta().getDisplayName().equals("Next")) {
                    final boolean isBuyInventory = title.contains("Buy");
                    final int page = Integer.parseInt(title.substring(title.length()-1));
                    final ShopCategory shopCategory = ShopCategory.getShopCategoryByInventoryName(title);
                    if(isPrevious) {
                        player.openInventory((page == 0) ? ShopCategoryInventory.getShopCategoryInventory(isBuyInventory) :
                                ShopInventory.getShopServerInventory(page-1, shopCategory, isBuyInventory));
                        return;
                    }
                    player.openInventory(ShopInventory.getShopServerInventory(page+1, shopCategory, isBuyInventory));
                }
            }
            return;
        }
    }
}
