package net.lyflow.skyblock.event.island;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.island.PlayerIslandRequest;
import net.lyflow.skyblock.utils.ResourceUtils;

import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.io.File;
import java.sql.SQLException;

public class CreateIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;

    public CreateIslandEvent(SkyBlock skyBlock, Player player, IslandDifficulty islandDifficulty) {
        try {
            final PlayerIslandRequest playerIslandRequest = new PlayerIslandRequest(skyBlock.getDatabase(), false);

            if(playerIslandRequest.playerHasIsland(player)) {
                player.sendMessage("§cTu ne peux pas avoir plusieurs îles en même temps !");
                setCancelled(true);
                return;
            }
            player.sendMessage("§bCréation de votre île en cours §6§o(difficulté : "+islandDifficulty.name()+")");

            // Set player has island in BDD
            playerIslandRequest.createIsland(player, islandDifficulty);
            skyBlock.getDatabase().closeConnection();

            skyBlock.getServer().getScheduler().runTaskAsynchronously(skyBlock, () -> {
                try {
                    // Make a copy of  Island World
                    final String defaultPath = "skyblock-map/"+playerIslandRequest.getPlayerID(player);
                    final File islandWorld = new File(skyBlock.getDataFolder(), "../../"+defaultPath);
                    ResourceUtils.saveResourceFolder("maps/skyblock-"+islandDifficulty.name().toLowerCase(), islandWorld, skyBlock, false);

                    // Load World
                    skyBlock.getServer().createWorld(new WorldCreator(defaultPath));

                    player.sendMessage("§bFin de la création de votre île");

                    // Teleport to the world
                    player.teleport(new Location(skyBlock.getServer().getWorld(defaultPath), -0.5, 100, 0.5, 90, 0));

                    player.sendMessage("§bTéléportation en cours");
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch(SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la base lors de la création d'une île", e);
        }
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean setCancelled) {
        this.isCancelled = setCancelled;
    }

}