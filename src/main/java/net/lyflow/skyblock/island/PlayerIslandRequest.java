package net.lyflow.skyblock.island;

import net.lyflow.skyblock.database.Database;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerIslandRequest {

    private final Database database;
    private final boolean autoClose;
    public PlayerIslandRequest(Database database, boolean autoClose) {
        this.database = database;
        this.autoClose = autoClose;
    }

    public int getPlayerID(Player player) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM Player WHERE UUID = ?");

        preparedStatement.setString(1, player.getUniqueId().toString());
        final ResultSet resultSet = preparedStatement.executeQuery();
        final int playerID = (resultSet.next()) ? resultSet.getInt(1) : -1;

        autoClose();

        return playerID;
    }

    public boolean playerHasIsland(Player player) throws SQLException {
        final Connection connection = database.getConnection();

        // Check if player has an island
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT COUNT(*) FROM Island_Mate
                    JOIN Player ON Player.id = Island_Mate.player_id
                    WHERE Player.UUID = ?
                    """);

        preparedStatement.setString(1, player.getUniqueId().toString());

        final boolean result = (0 != preparedStatement.executeQuery().getInt(1));

        autoClose();

        return result;
    }

    public void createIsland(Player player, IslandDifficulty islandDifficulty) throws SQLException {
        final Connection connection = database.getConnection();

        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Island (id_difficulty) VALUES (?)");
        preparedStatement.setInt(1, islandDifficulty.getDifficulty());
        preparedStatement.executeUpdate();

        final int primaryKey = preparedStatement.getGeneratedKeys().getInt(1);

        final PreparedStatement preparedStatement2 = connection.prepareStatement("""
            INSERT INTO Island_Mate VALUES (?, (SELECT Player.id FROM Player WHERE Player.UUID = ?), ?)
            """);
        preparedStatement2.setInt(1, primaryKey);
        preparedStatement2.setString(2, player.getUniqueId().toString());
        preparedStatement2.setInt(3, MateStatus.OWNER.getID());

        preparedStatement2.execute();

        autoClose();
    }

    public List<IslandMate> islandMates(Player player) throws SQLException {
        final List<IslandMate> islandMates = new ArrayList<>();
        final Connection connection = database.getConnection();

        final  PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT Player.UUID, Island_Mate.status FROM Island_Mate
                JOIN Player ON Player.id = Island_Mate.player_id
                WHERE island_id = (
                SELECT Island_Mate.island_id FROM Island_Mate
                JOIN Player ON Player.id = Island_Mate.player_id
                WHERE Player.UUID = ?
                )
                """);

        preparedStatement.setString(1, player.getUniqueId().toString());

        final ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            islandMates.add(new IslandMate(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString(1))),
                    MateStatus.getMateStatusByID(resultSet.getInt(2))));
        }

        return islandMates;
    }

    private void autoClose() throws SQLException {
        if (autoClose) database.closeConnection();
    }

}
