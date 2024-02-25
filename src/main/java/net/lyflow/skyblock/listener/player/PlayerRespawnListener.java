package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.sql.SQLException;
import java.util.logging.Level;

public class PlayerRespawnListener implements Listener {

    private final SkyBlock skyblock;

    public PlayerRespawnListener(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
        try {
            if (islandRequest.hasIsland(player.getUniqueId()))
                event.setRespawnLocation(islandRequest.getSpawnLocation(islandRequest.getIslandID(player.getUniqueId())));
            skyblock.getDatabase().closeConnection();
        } catch (SQLException e) {
            skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
