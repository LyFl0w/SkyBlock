package net.lyflow.skyblock.request.island;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.island.IslandMate;
import net.lyflow.skyblock.island.MateStatus;
import net.lyflow.skyblock.request.DefaultRequest;
import net.lyflow.skyblock.utils.LocationUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandRequest extends DefaultRequest {

    public IslandRequest(Database database, boolean autoClose) {
        super(database, autoClose);
    }

    public boolean hasIsland(UUID uuid) throws SQLException {
        final Connection connection = database.getConnection();

        // Check if player has an island
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT COUNT(*) FROM Island_Mate
                    JOIN Player ON Player.id = Island_Mate.player_id
                    WHERE Player.UUID = ?
                    """);

        preparedStatement.setString(1, uuid.toString());

        final boolean result = (0 != preparedStatement.executeQuery().getInt(1));

        autoClose();

        return result;
    }

    public int getIslandID(UUID uuid) throws SQLException {
        final Connection connection = database.getConnection();

        // Check if player has an island
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                    SELECT Island_Mate.island_id FROM Island_Mate
                    JOIN Player ON Player.id = Island_Mate.player_id
                    WHERE Player.UUID = ?
                    """);

        preparedStatement.setString(1, uuid.toString());

        final int islandID = preparedStatement.executeQuery().getInt(1);

        autoClose();

        return islandID;
    }

    public void createIsland(UUID uuid, Location spawn, IslandDifficulty islandDifficulty) throws SQLException {
        final Connection connection = database.getConnection();

        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Island (id_difficulty, spawn_location) VALUES (?, ?)");
        preparedStatement.setInt(1, islandDifficulty.getDifficulty());
        preparedStatement.setString(2, LocationUtils.getStringFromLocation(spawn));
        preparedStatement.executeUpdate();

        final int primaryKey = preparedStatement.getGeneratedKeys().getInt(1);

        final PreparedStatement preparedStatement2 = connection.prepareStatement("""
            INSERT INTO Island_Mate VALUES (?, (SELECT Player.id FROM Player WHERE Player.UUID = ?), ?)
            """);
        preparedStatement2.setInt(1, primaryKey);
        preparedStatement2.setString(2, uuid.toString());
        preparedStatement2.setInt(3, MateStatus.OWNER.getID());

        preparedStatement2.execute();

        autoClose();
    }

    public void addMate(UUID uuid, int islandID) throws SQLException {
        final Connection connection = database.getConnection();

        final PreparedStatement preparedStatement = connection.prepareStatement("""
            INSERT INTO Island_Mate VALUES (?, (SELECT Player.id FROM Player WHERE Player.UUID = ?), ?)
            """);
        preparedStatement.setInt(1, islandID);
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.setInt(3, MateStatus.MATE.getID());

        preparedStatement.execute();

        autoClose();
    }

    public List<IslandMate> getMates(UUID uuid) throws SQLException {
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

        preparedStatement.setString(1, uuid.toString());

        final ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            islandMates.add(new IslandMate(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString(1))),
                    MateStatus.getMateStatusByID(resultSet.getInt(2))));
        }

        autoClose();

        return islandMates;
    }

    public String getSpawnLocationFormattedString(int islandID) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT spawn_location FROM Island WHERE id = ?");
        preparedStatement.setInt(1, islandID);

        final String locationString = preparedStatement.executeQuery().getString(1);

        autoClose();

        return locationString;
    }

    public Location getSpawnLocation(int islandID) throws SQLException {
        final Location location = LocationUtils.getLocationFromString(getSpawnLocationFormattedString(islandID));

        autoClose();

        return location;
    }

    public String getIslandWorldName(int islandID) throws SQLException {
        final String worldName = getSpawnLocationFormattedString(islandID).split(":", 2)[0];

        autoClose();

        return worldName;
    }

}
