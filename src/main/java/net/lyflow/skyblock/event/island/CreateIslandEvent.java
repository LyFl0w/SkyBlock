package net.lyflow.skyblock.event.island;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.island.PlayerIslandRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.sql.SQLException;

public class CreateIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;

    public CreateIslandEvent(SkyBlock skyBlock, Player player, IslandDifficulty islandDifficulty) {
        try {
            final PlayerIslandRequest playerIslandRequest = new PlayerIslandRequest(skyBlock.getDatabase(), false);

            if(playerIslandRequest.playerHasIsland(player)) {
                player.sendMessage("Tu ne peux pas avoir plusieurs îles en même temps !");
                setCancelled(true);
                return;
            }

            playerIslandRequest.createIsland(player, islandDifficulty);

            skyBlock.getDatabase().closeConnection();

            player.sendMessage("§bCréation de votre île en cours §6§o(difficulté : "+islandDifficulty.name()+")");
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
