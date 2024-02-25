package net.lyflow.skyblock.listener.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.security.SecureRandom;

public class PlayerToggleSneakListener implements Listener {

    private static final int RADIUS = 3;

    @EventHandler
    public void onPLayerToggleSneak(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (!event.isSneaking()) return;

        final Location location = player.getLocation();
        if (!location.getWorld().getName().contains("skyblock-map")) return;

        for (double x = -RADIUS; x <= RADIUS; x++) {
            for (double y = -RADIUS; y <= RADIUS; y++) {
                for (double z = -RADIUS; z <= RADIUS; z++) {
                    final Block block = location.clone().add(x, y, z).getBlock();
                    if (((block.getBlockData() instanceof Sapling && !(block.getBlockData() instanceof Bamboo)) || block.getType() == Material.MANGROVE_PROPAGULE) && new SecureRandom().nextInt(17) == 8)
                        block.applyBoneMeal(BlockFace.UP);
                }
            }
        }
    }
}
