package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.database.request.island.UpgradeIslandRequest;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
        skyblock.getServer().getScheduler().runTask(skyblock, () -> {
            final UUID playerUUID = event.getUniqueId();
            final IslandRequest islandRequest = new IslandRequest(skyblock.getDatabase(), false);
            try {
                // LOAD ISLAND WORLD IF IT'S NOT LOADED
                if (islandRequest.hasIsland(playerUUID)) {
                    final int islandID = islandRequest.getIslandID(playerUUID);
                    final String worldName = islandRequest.getIslandWorldName(islandID);
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
                        final UpgradeIslandRequest upgradeIslandRequest = new UpgradeIslandRequest(skyblock.getDatabase(), false);
                        final Map<Integer, IslandUpgradeStatus> alreadyUpgrades = upgradeIslandRequest.getIslandUpgrades(islandID);
                        final IslandUpgradeManager islandUpgradeManager = skyblock.getIslandUpgradeManager();

                        // LOAD CURRENT UPGRADE
                        islandUpgradeManager.getIslandUpgrades().stream().parallel().filter(islandUpgrade -> alreadyUpgrades.containsKey(islandUpgrade.getID()))
                                .forEach(islandUpgrade -> islandUpgrade.getIslandUpgradeStatusManager().loadIslandUpgrade(islandID, alreadyUpgrades.get(islandUpgrade.getID())));

                        final List<IslandUpgrade> islandUpgrades = islandUpgradeManager.getIslandUpgrades().stream().parallel()
                                .filter(islandUpgrade -> !alreadyUpgrades.containsKey(islandUpgrade.getID())).toList();

                        // INIT NEW UPGRADE AVAILABLE
                        final boolean newUpgradeAvailable = !islandUpgrades.isEmpty();
                        if (newUpgradeAvailable) {
                            final HashMap<Integer, IslandUpgradeStatus> toSave = new HashMap<>();
                            final IslandUpgradeStatus islandUpgradeStatus = new IslandUpgradeStatus();

                            islandUpgrades.forEach(islandUpgrade -> islandUpgrade.getIslandUpgradeStatusManager().loadIslandUpgrade(islandID, islandUpgradeStatus));
                            islandUpgrades.stream().parallel().forEach(islandUpgrade -> toSave.put(islandUpgrade.getID(), islandUpgradeStatus));

                            upgradeIslandRequest.addNewIslandUpgrade(islandID, toSave);
                        }

                        skyblock.getDatabase().closeConnection();

                        skyblock.getServer().createWorld(new WorldCreator(worldName));

                        if (newUpgradeAvailable) {
                            skyblock.getServer().getScheduler().runTaskLater(skyblock, () -> {
                                final OfflinePlayer offlinePlayer = skyblock.getServer().getOfflinePlayer(playerUUID);
                                if (!offlinePlayer.isOnline()) return;
                                offlinePlayer.getPlayer().sendMessage("§aDe nouveaux améliorations d'îles sont disponibles");
                            }, 20L * 5);
                        }

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