package net.lyflow.skyblock.event.island;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.island.MateStatus;
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

    public CreateIslandEvent(SkyBlock skyBlock, Player player, IslandDifficulty islandDifficulty) {
        try {
            final Connection connection = skyBlock.getDatabase().getConnection();

            // Check if player has an island
            final PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT COUNT(*) FROM Island_Mate
                    JOIN Player ON Player.id = Island_Mate.player_id
                    WHERE Player.UUID = ?
                    """);

            preparedStatement.setString(1, player.getUniqueId().toString());

            final boolean playerHasIsland = (0 != preparedStatement.executeQuery().getInt(1));

            if(playerHasIsland) {
                player.sendMessage("Tu ne peux pas avoir plusieurs îles en même temps !");
                connection.close();

                setCancelled(true);
                return;
            }

            final PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Island (id_difficulty) VALUES (?)");
            preparedStatement2.setInt(1, islandDifficulty.getDifficulty());
            preparedStatement2.executeUpdate();

            final int primaryKey = preparedStatement2.getGeneratedKeys().getInt(1);

            final PreparedStatement preparedStatementPlayerID = connection.prepareStatement("SELECT id FROM Player WHERE UUID = ?");
            preparedStatementPlayerID.setString(1, player.getUniqueId().toString());

            final PreparedStatement preparedStatement3 = connection.prepareStatement("INSERT INTO Island_Mate VALUES (?, ?, ?)");
            preparedStatement3.setInt(1, primaryKey);
            preparedStatement3.setInt(2, preparedStatementPlayerID.executeQuery().getInt(1));
            preparedStatement3.setInt(3, MateStatus.OWNER.getID());

            preparedStatement3.execute();

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
