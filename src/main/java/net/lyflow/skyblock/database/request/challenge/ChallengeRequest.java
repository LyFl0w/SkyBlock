package net.lyflow.skyblock.database.request.challenge;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.DefaultRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChallengeRequest extends DefaultRequest {

    public ChallengeRequest(Database database, boolean autoClose) {
        super(database, autoClose);
    }

    public Map<Integer, String> getChallengesDataSerialized(UUID uuid) throws SQLException {
        final Map<Integer, String> challengesData = new HashMap<>();
        final Connection connection = database.getConnection();

        try (final PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT challenge_id, progress FROM Challenge
                WHERE player_id = (
                SELECT id FROM Player WHERE UUID = ?
                )
                """)) {

            preparedStatement.setString(1, uuid.toString());
            final ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
                challengesData.put(resultSet.getInt(1), resultSet.getString(2));
        }

        autoClose();

        return challengesData;
    }

    public void updateChallenge(int challengeID, UUID playerUUID, PlayerChallenge playerChallengeProgress) throws SQLException {
        final Connection connection = database.getConnection();
        try (final PreparedStatement preparedStatement = connection.prepareStatement("""
                UPDATE Challenge SET progress = ?
                WHERE challenge_id = ? AND player_id = (
                SELECT id FROM Player WHERE UUID = ?
                )
                """)) {

            preparedStatement.setString(1, playerChallengeProgress.serialize());
            preparedStatement.setInt(2, challengeID);
            preparedStatement.setString(3, playerUUID.toString());
            preparedStatement.execute();
        }

        autoClose();
    }
}
