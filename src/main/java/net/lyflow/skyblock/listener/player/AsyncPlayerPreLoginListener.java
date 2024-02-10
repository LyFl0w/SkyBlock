package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class AsyncPlayerPreLoginListener implements Listener {

    private final SkyBlock skyblock;

    public AsyncPlayerPreLoginListener(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Bukkit.getScheduler().runTask(skyblock, () -> {
            final UUID playerUUID = event.getUniqueId();
            final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
            try {
                // LOAD ISLAND WORLD IF IT'S NOT LOADED
                if (islandRequest.hasIsland(playerUUID)) {
                    final String worldName = islandRequest.getIslandWorldName(islandRequest.getIslandID(playerUUID));
                    final Map<String, Integer> unloadWorlds = PlayerQuitListener.getUnloadWorlds();
                    if (unloadWorlds.containsKey(worldName)) {
                        // REMOVE TASK WHO UNLOAD ISLAND WORLD
                        skyblock.getServer().getScheduler().cancelTask(unloadWorlds.get(worldName));
                        // REMOVE ISLAND WORLD OF UNLOAD WORLDS LIST
                        unloadWorlds.remove(worldName);

                        skyblock.getDatabase().closeConnection();
                        return;
                    }

                    if (skyblock.getServer().getWorld(worldName) == null) {
                        skyblock.getServer().createWorld(new WorldCreator(worldName));
                        skyblock.getDatabase().closeConnection();
                        return;
                    }
                }
                skyblock.getDatabase().closeConnection();
            } catch (SQLException e) {
                skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
            }
        });
    }

}