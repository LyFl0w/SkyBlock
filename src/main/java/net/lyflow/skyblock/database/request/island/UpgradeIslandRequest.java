package net.lyflow.skyblock.database.request.island;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.DefaultRequest;
import net.lyflow.skyblock.upgrade.IslandUpgradeStatus;

import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UpgradeIslandRequest extends DefaultRequest {

    public UpgradeIslandRequest(Database database, boolean autoClose) {
        super(database, autoClose);
    }

    @Nullable
    public IslandUpgradeStatus getIslandUpgradeStatus(int islandID, int upgradeID) throws SQLException {
        final IslandUpgradeStatus islandUpgradeStatus;
        try (final PreparedStatement preparedStatement = database.getConnection().prepareStatement("""
                SELECT buy, status FROM Island_Upgrade
                WHERE island_id = ? AND upgrade_id = ?
                """)) {


            preparedStatement.setInt(1, islandID);
            preparedStatement.setInt(2, upgradeID);

            final ResultSet resultSet = preparedStatement.executeQuery();

            islandUpgradeStatus = (resultSet.next()
                    ? new IslandUpgradeStatus((resultSet.getInt(1) != 0), (resultSet.getInt(2) != 0))
                    : null);
        }

        autoClose();

        return islandUpgradeStatus;
    }

    public Map<Integer, IslandUpgradeStatus> getIslandUpgrades(int islandID) throws SQLException {
        final HashMap<Integer, IslandUpgradeStatus> upgrades = new HashMap<>();

        try (final PreparedStatement preparedStatement = database.getConnection().prepareStatement("""
                SELECT upgrade_id, buy, status FROM Island_Upgrade
                WHERE island_id = ?
                """)) {
            preparedStatement.setInt(1, islandID);

            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                upgrades.put(resultSet.getInt(1), new IslandUpgradeStatus((resultSet.getInt(2) != 0), (resultSet.getInt(3) != 0)));
        }

        autoClose();

        return upgrades;
    }

    public void updateIslandUpgrade(int islandID, int upgradeID, IslandUpgradeStatus upgradeStatus) throws SQLException {
        try (final PreparedStatement preparedStatement = database.getConnection().prepareStatement("""
                UPDATE Island_Upgrade SET buy = ?, status = ?
                WHERE island_id = ? AND upgrade_id = ?
                """)) {

            preparedStatement.setInt(1, (upgradeStatus.isBuy() ? 1 : 0));
            preparedStatement.setInt(2, (upgradeStatus.isEnable() ? 1 : 0));
            preparedStatement.setInt(3, islandID);
            preparedStatement.setInt(4, upgradeID);

            preparedStatement.execute();
        }

        autoClose();
    }

    public void addNewIslandUpgrade(int islandID, Map<Integer, IslandUpgradeStatus> newIslandUpgrade) throws SQLException {
        try (final Connection connection = database.getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Island_Upgrade VALUES (?, ?, ?, ?)")) {

            connection.setAutoCommit(false);
            newIslandUpgrade.forEach((key, value) -> {
                try {
                    preparedStatement.setInt(1, islandID);
                    preparedStatement.setInt(2, key);
                    preparedStatement.setInt(3, (value.isBuy() ? 1 : 0));
                    preparedStatement.setInt(4, (value.isEnable() ? 1 : 0));

                    preparedStatement.addBatch();
                } catch (SQLException e) {
                    throw new IllegalCallerException(e);
                }
            });

            preparedStatement.executeBatch();
            connection.commit();
        }

        autoClose();
    }

    public void deleteIsland(int id) throws SQLException {
        try (final PreparedStatement preparedStatement = database.getConnection().prepareStatement("DELETE FROM Island_Upgrade WHERE island_id = ?")) {
            preparedStatement.setInt(1, id);

            preparedStatement.execute();
        }

        autoClose();
    }

}
