package net.quillcraft.skyblock.database.request.challenge;

import net.quillcraft.skyblock.challenge.PlayerChallengeProgress;
import net.quillcraft.skyblock.database.Database;
import net.quillcraft.skyblock.database.request.DefaultRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ChallengeRequest extends DefaultRequest {

    public ChallengeRequest(Database database, boolean autoClose) {
        super(database, autoClose);
    }

    @Deprecated
    public String getChallengeData(int challengeID, UUID uuid) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT data FROM Challenge
                WHERE challenge_id = ? AND player_id = (
                SELECT id FROM Player WHERE UUID = ?
                )
                """);

        preparedStatement.setInt(1, challengeID);
        preparedStatement.setString(2, uuid.toString());
        final ResultSet resultSet = preparedStatement.executeQuery();
        final String data = (resultSet.next()) ? resultSet.getString(1) : null;

        autoClose();

        return data;
    }

    public HashMap<Integer, String> getChallengesDataSerialized(UUID uuid) throws SQLException {
        final HashMap<Integer, String> challengesData = new HashMap<>();
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT challenge_id, progress FROM Challenge
                WHERE player_id = (
                SELECT id FROM Player WHERE UUID = ?
                )
                """);

        preparedStatement.setString(1, uuid.toString());
        final ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) challengesData.put(resultSet.getInt(1), resultSet.getString(2));

        autoClose();

        return challengesData;
    }

    public void updateChallenge(int challengeID, UUID playerUUID, PlayerChallengeProgress playerChallengeProgress) throws SQLException {
        final Connection connection = database.getConnection();
        final PreparedStatement preparedStatement = connection.prepareStatement("""
                UPDATE Challenge SET progress = ?
                WHERE challenge_id = ? AND player_id = (
                SELECT id FROM Player WHERE UUID = ?
                )
                """);

        preparedStatement.setString(1, playerChallengeProgress.serialize());
        preparedStatement.setInt(2, challengeID);
        preparedStatement.setString(3, playerUUID.toString());
        preparedStatement.execute();

        autoClose();
    }
}
