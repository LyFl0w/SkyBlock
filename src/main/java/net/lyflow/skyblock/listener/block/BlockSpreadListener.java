package net.lyflow.skyblock.listener.block;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.security.SecureRandom;

public class BlockSpreadListener implements Listener {

    @EventHandler
    public void onBlockSpread(BlockFormEvent event) {
        final Material dmaterial = event.getNewState().getType();

        Bukkit.getLogger().warning("Form "+dmaterial);
        if(dmaterial == Material.COBBLESTONE && new SecureRandom().nextInt(10)+1 == 5) {
            Bukkit.getLogger().warning("Rdm");
            final int rdm = new SecureRandom().nextInt(33)+1;
            Material material;
            if(rdm <= 15) material = Material.COPPER_ORE;
            else if(rdm <= 25) material = Material.IRON_ORE;
            else if(rdm <= 30) material = Material.GOLD_ORE;
            else if(rdm <= 32) material = Material.DIAMOND_ORE;
            else material = Material.EMERALD_ORE;

            event.getNewState().setType(material);
        }
    }

}
