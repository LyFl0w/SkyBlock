package net.lyflow.skyblock.event.island;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.request.account.AccountRequest;
import net.lyflow.skyblock.request.island.IslandRequest;
import net.lyflow.skyblock.utils.ResourceUtils;

import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.SQLException;

public class CreateIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;

    public CreateIslandEvent(SkyBlock skyBlock, Player player, IslandDifficulty islandDifficulty) {
        try {
            final IslandRequest islandRequest = new IslandRequest(skyBlock.getDatabase(), false);

            if(islandRequest.hasIsland(player.getUniqueId())) {
                player.sendMessage("§cTu ne peux pas avoir plusieurs îles en même temps !");
                setCancelled(true);
                return;
            }
            player.sendMessage("§bCréation de votre île en cours §6§o(difficulté : "+islandDifficulty.name()+")");

            try {
                // Make a copy of  Island World
                final String defaultPath = "skyblock-map/"+new AccountRequest(skyBlock.getDatabase(), true).getPlayerID(player);
                final File islandWorld = new File(skyBlock.getDataFolder(), "../../"+defaultPath);
                ResourceUtils.saveResourceFolder("maps/skyblock-"+islandDifficulty.name().toLowerCase(), islandWorld, skyBlock, false);

                // Load World
                skyBlock.getServer().createWorld(new WorldCreator(defaultPath));
                final Location spawn = new Location(skyBlock.getServer().getWorld(defaultPath), -0.5, 100, 0.5, 90, 0);

                player.sendMessage("§bTéléportation en cours");

                try {
                    // create island in DB
                    islandRequest.createIsland(player.getUniqueId(), spawn, islandDifficulty);
                    skyBlock.getDatabase().closeConnection();

                    // Teleport to the world
                    player.teleport(spawn);
                } catch(SQLException e) {
                    // DELETE USELESS WORLD FOLDER IF WE CAN'T GENERATE UTILS INFORMATION IN DATABASE
                    islandWorld.delete();
                    throw new RuntimeException("Database error (therefore the world folder ("+defaultPath+") has been deleted)", e);
                }
            } catch(SQLException e) {
                    throw new RuntimeException("Player ID not found", e);
            }
        } catch(SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de la base lors de la création d'une île", e);
        }
    }

    @Override @NotNull
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