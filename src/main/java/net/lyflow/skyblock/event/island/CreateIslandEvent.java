package net.lyflow.skyblock.event.island;

import net.lyflow.skyblock.SkyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateIslandEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;

    public CreateIslandEvent(SkyBlock skyBlock, Player player) {
        try {
            final Connection connection = skyBlock.getDatabase().getConnection();

            // Check if player has an island
            final PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT COUNT(*) FROM Island_Mates
                    JOIN Player ON Player.id = Island_Mates.player_id
                    WHERE Player.id = ?
                    """);

            final boolean playerHasIsland = (0 != preparedStatement.executeQuery().getInt(1));

            if(playerHasIsland) {
                player.sendMessage("Tu ne peux pas avoir plusieurs iles en même temps !");
                connection.close();

                setCancelled(true);
                return;
            }

            connection.close();
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
