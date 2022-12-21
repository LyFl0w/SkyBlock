package net.lyflow.skyblock.database.request.challenge;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.DefaultRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ChallengeRequest extends DefaultRequest {

    public ChallengeRequest(Database database, boolean autoClose) {
        super(database, autoClose);
    }

    public String getChallengesData(int challengeID, UUID uuid) throws SQLException {
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

}
