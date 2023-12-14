package net.quillcraft.skyblock.listener.player;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.database.request.island.IslandRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.sql.SQLException;

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
            throw new RuntimeException(e);
        }
    }
}
