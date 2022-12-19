package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;

import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class AsyncPlayerPreLoginListener implements Listener {

    private final SkyBlock skyBlock;

    public AsyncPlayerPreLoginListener(SkyBlock skyblock) {
        this.skyBlock = skyblock;
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        final UUID playerUUID = event.getUniqueId();
        final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);
        try {
            // LOAD ISLAND WORLD IF IT'S NOT LOADED
            if(islandRequest.hasIsland(playerUUID)) {
                final String worldName = islandRequest.getIslandWorldName(islandRequest.getIslandID(playerUUID));
                final HashMap<String, Integer> unloadWorlds = PlayerQuitListener.getUnloadWorlds();
                if(unloadWorlds.containsKey(worldName)) {
                    // REMOVE TASK WHO UNLOAD ISLAND WORLD
                    skyBlock.getServer().getScheduler().cancelTask(unloadWorlds.get(worldName));
                    // REMOVE ISLAND WORLD OF UNLOAD WORLDS LIST
                    unloadWorlds.remove(worldName);

                    skyBlock.getDatabase().closeConnection();
                    return;
                }

                if(skyBlock.getServer().getWorld(worldName) == null){
                    skyBlock.getServer().getScheduler().runTask(skyBlock, () -> skyBlock.getServer().createWorld(new WorldCreator(worldName)));
                    skyBlock.getDatabase().closeConnection();
                    return;
                }

            }

            skyBlock.getDatabase().closeConnection();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}