package net.lyflow.skyblock.request.account;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.request.DefaultRequest;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AccountRequest extends DefaultRequest {

    public AccountRequest(Database database, boolean autoClose) {
        super(database, autoClose);
    }

    @Nullable
    public UUID getPlayerUUIDByName(String name) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT UUID FROM Player WHERE name = ?");

        preparedStatement.setString(1, name);
        final ResultSet resultSet = preparedStatement.executeQuery();
        final UUID uuid = (resultSet.next()) ? UUID.fromString(resultSet.getString(1)) : null;

        autoClose();

        return uuid;
    }

    @Nullable
    public OfflinePlayer getOfflinePlayerByName(String name) throws SQLException {
        final UUID uuid = getPlayerUUIDByName(name);
        return (uuid == null) ? null : Bukkit.getOfflinePlayer(uuid);
    }

    public int getPlayerID(OfflinePlayer offlinePlayer) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM Player WHERE UUID = ?");

        preparedStatement.setString(1, offlinePlayer.getUniqueId().toString());
        final ResultSet resultSet = preparedStatement.executeQuery();
        final int playerID = (resultSet.next()) ? resultSet.getInt(1) : -1;

        autoClose();

        return playerID;
    }

    public boolean hasAccount(UUID uuid) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM Player WHERE UUID = ?");

        preparedStatement.setString(1, uuid.toString());

        // Check if player has an account
        final boolean hasAccount = (0 != preparedStatement.executeQuery().getInt(1));

        autoClose();

        return hasAccount;
    }

    public void createPlayerAccount(Player player) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Player (UUID, name) VALUES (?, ?)");
        preparedStatement.setString(1, player.getUniqueId().toString());
        preparedStatement.setString(2, player.getName());
        preparedStatement.execute();

        autoClose();
    }

    public void updatePlayerName(Player player) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Player SET name = ? WHERE UUID = ?");
        preparedStatement.setString(1, player.getName());
        preparedStatement.setString(2, player.getUniqueId().toString());

        preparedStatement.execute();

        autoClose();
    }

}
