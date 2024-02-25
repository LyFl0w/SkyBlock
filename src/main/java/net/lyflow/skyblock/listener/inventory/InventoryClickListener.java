package net.lyflow.skyblock.listener.inventory;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.inventory.challenge.ChallengeInventory;
import net.lyflow.skyblock.inventory.island.IslandDifficultyInventory;
import net.lyflow.skyblock.inventory.shop.AmountItemShopInventory;
import net.lyflow.skyblock.inventory.shop.ShopCategoryInventory;
import net.lyflow.skyblock.inventory.shop.ShopInventory;

import net.lyflow.skyblock.inventory.upgrade.UpgradeInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


public class InventoryClickListener implements Listener {

    private final SkyBlock skyBlock;
    public InventoryClickListener(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final Inventory inventory = event.getClickedInventory();
        final ItemStack item = event.getCurrentItem();

        if(inventory == null || inventory == player.getInventory() || item == null || item.getType() == Material.AIR) return;

        final String title = event.getView().getTitle();

        switch (title) {
            case "§6Difficulté de l'île" -> IslandDifficultyInventory.inventoryEvent(skyBlock, event, player, item);
            case "§gChallenges - Menu" -> ChallengeInventory.inventoryDifficultyChallengeEvent(skyBlock, event, player);
            case "§9Shop" -> ShopCategoryInventory.inventoryEvent(event, player, item);
            case "§aShop" -> ShopInventory.inventoryEvent(event, player, item);
            case "§6Upgrade" -> UpgradeInventory.inventoryEvent(skyBlock, event, player);
            default -> inventoryStartWith(skyBlock, event, inventory, title, player, item);
        }

    }

    private static void inventoryStartWith(SkyBlock skyBlock, InventoryClickEvent event, Inventory inventory, String title, Player player, ItemStack item) {
        if(title.startsWith("§aShop/Category")) {
            ShopCategoryInventory.inventoryCategoryEvent(title, event, player, item);
            return;
        }

        if(title.startsWith("§aShop/Amount")) {
            AmountItemShopInventory.inventoryEvent(skyBlock, title, inventory, event, player, item);
            return;
        }

        if(title.startsWith("§aShop")) {
            ShopInventory.inventorySwitchPageEvent(skyBlock, event, title, player, item);
            return;
        }

        if(title.startsWith("§gChallenges")) {
            ChallengeInventory.inventoryDifficultyChallengeEvent(skyBlock, event, title, player);
        }
    }
}
