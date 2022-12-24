package net.lyflow.skyblock.listener;

import net.lyflow.skyblock.SkyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class CraftItemListener implements Listener {

    private final SkyBlock skyBlock;
    public CraftItemListener(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        final ItemStack itemStack = event.getCurrentItem();
        if(event.getCurrentItem() == null) return;

        final Player player = (Player) event.getWhoClicked();

    }
}
