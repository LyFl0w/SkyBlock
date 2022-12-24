package net.lyflow.skyblock.listener.player;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.security.SecureRandom;

public class PlayerToggleSneakListener implements Listener {

    private final static int radius = 3;

    @EventHandler
    public void onPLayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if(!event.isSneaking()) return;

        final Location location = player.getLocation();
        if(!location.getWorld().getName().contains("skyBlock-map")) return;

        for(double x=-radius; x<=radius; x++) {
            for(double y=-radius; y<=radius; y++) {
                for(double z=-radius; z<=radius; z++) {
                    final Block block = location.clone().add(x, y, z).getBlock();
                    if(block.getBlockData() instanceof Sapling && new SecureRandom().nextInt(17) == 8)
                        block.applyBoneMeal(BlockFace.UP);
                }
            }
        }
    }
}
