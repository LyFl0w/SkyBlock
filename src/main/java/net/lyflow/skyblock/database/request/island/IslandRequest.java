package net.lyflow.skyblock.database.request.island;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.DefaultRequest;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.island.IslandMate;
import net.lyflow.skyblock.island.PlayerIslandStatus;
import net.lyflow.skyblock.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

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

        final ResultSet resultSet = preparedStatement.executeQuery();
        final int islandID = (resultSet.next()) ? resultSet.getInt(1) : -1;

        autoClose();

        return islandID;
    }

    public int createIsland(UUID uuid, IslandDifficulty islandDifficulty, String worldPath, double x, double y, double z, float yaw, float pitch) throws SQLException {
        final Connection connection = database.getConnection();

        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Island (id_difficulty) VALUES (?)");
        preparedStatement.setInt(1, islandDifficulty.getDifficulty());
        preparedStatement.executeUpdate();

        final int primaryKey = preparedStatement.getGeneratedKeys().getInt(1);

        final PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE Island SET spawn_location = ? WHERE id = ?");
        preparedStatement2.setString(1, LocationUtils.getStringFromPosition(worldPath + (worldPath.endsWith("/") ? "" : "/") + primaryKey, x, y, z, yaw, pitch));
        preparedStatement2.setInt(2, primaryKey);

        preparedStatement2.execute();

        final PreparedStatement preparedStatement3 = connection.prepareStatement("""
                INSERT INTO Island_Mate VALUES (?, (SELECT Player.id FROM Player WHERE Player.UUID = ?), ?)
                """);
        preparedStatement3.setInt(1, primaryKey);
        preparedStatement3.setString(2, uuid.toString());
        preparedStatement3.setInt(3, PlayerIslandStatus.OWNER.getID());

        preparedStatement3.execute();

        autoClose();

        return primaryKey;
    }

    public void addMate(UUID uuid, int islandID) throws SQLException {
        final Connection connection = database.getConnection();

        final PreparedStatement preparedStatement = connection.prepareStatement("""
                INSERT INTO Island_Mate VALUES (?, (SELECT Player.id FROM Player WHERE Player.UUID = ?), ?)
                """);
        preparedStatement.setInt(1, islandID);
        preparedStatement.setString(2, uuid.toString());
        preparedStatement.setInt(3, PlayerIslandStatus.MATE.getID());

        preparedStatement.execute();

        autoClose();
    }

    public List<IslandMate> getMates(UUID uuid) throws SQLException {
        final List<IslandMate> islandMates = new ArrayList<>();
        final Connection connection = database.getConnection();

        final PreparedStatement preparedStatement = connection.prepareStatement("""
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
        while (resultSet.next()) {
            islandMates.add(new IslandMate(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString(1))),
                    PlayerIslandStatus.getMateStatusByID(resultSet.getInt(2))));
        }

        autoClose();

        return islandMates;
    }

    @Nullable
    public String getSpawnLocationFormattedString(int islandID) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT spawn_location FROM Island WHERE id = ?");
        preparedStatement.setInt(1, islandID);

        final ResultSet resultSet = preparedStatement.executeQuery();
        final String locationString = (resultSet.next()) ? resultSet.getString(1) : null;

        autoClose();

        return locationString;
    }

    @Nullable
    public Location getSpawnLocation(int islandID) throws SQLException {
        final String locationFormatted = getSpawnLocationFormattedString(islandID);

        return (locationFormatted != null) ? LocationUtils.getLocationFromString(locationFormatted) : null;
    }

    @Nullable
    public String getIslandWorldName(int islandID) throws SQLException {
        final String worldName = getSpawnLocationFormattedString(islandID);

        return (worldName != null) ? worldName.split(":", 2)[0] : null;
    }

    @Nullable
    public PlayerIslandStatus getPlayerIslandStatus(UUID uuid) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT status FROM Island_Mate
                JOIN Player ON Player.id = Island_Mate.player_id
                WHERE Player.UUID = ?
                """);
        preparedStatement.setString(1, uuid.toString());

        final ResultSet resultSet = preparedStatement.executeQuery();
        final PlayerIslandStatus playerIslandStatus = (resultSet.next()) ? PlayerIslandStatus.getMateStatusByID(resultSet.getInt(1)) : null;

        autoClose();

        return playerIslandStatus;
    }

    public void setPlayerIslandStatus(UUID uuid, PlayerIslandStatus playerIslandStatus) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                UPDATE Island_Mate SET status = ?
                WHERE player_id = (SELECT id FROM Player WHERE Player.UUID = ?)
                """);
        preparedStatement.setInt(1, playerIslandStatus.getID());
        preparedStatement.setString(2, uuid.toString());

        preparedStatement.execute();

        autoClose();
    }

    public void setIslandSpawn(int islandID, Location location) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Island SET spawn_location = ? WHERE id = ?");
        preparedStatement.setString(1, LocationUtils.getStringFromLocation(location));
        preparedStatement.setInt(2, islandID);

        preparedStatement.execute();

        autoClose();
    }

    public void leaveIsland(UUID uuid) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                DELETE FROM Island_Mate
                WHERE player_id = (SELECT id FROM Player WHERE UUID = ?)
                """);
        preparedStatement.setString(1, uuid.toString());

        preparedStatement.execute();

        autoClose();
    }

    public void deleteIsland(int id) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Island WHERE id = ?");
        preparedStatement.setInt(1, id);

        preparedStatement.execute();

        autoClose();
    }
}
