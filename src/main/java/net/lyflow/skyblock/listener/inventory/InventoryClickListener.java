package net.lyflow.skyblock.listener.inventory;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.event.island.CreateIslandEvent;
import net.lyflow.skyblock.island.IslandDifficulty;
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
        }
    }
}
