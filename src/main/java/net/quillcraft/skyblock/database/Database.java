package net.quillcraft.skyblock.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {

    private final JavaPlugin javaPlugin;
    private final Logger logger;

    private final String databaseLink;
    private Connection connection;

    public Database(JavaPlugin javaPlugin, String databaseName) {
        this.javaPlugin = javaPlugin;
        this.logger = javaPlugin.getLogger();

        final File database = new File(javaPlugin.getDataFolder(), databaseName);
        this.databaseLink = "jdbc:sqlite:" + database.getPath();

        initDatabase(database);
    }

    private void initDatabase(File database) {
        // Generate database if not exist
        if (!database.exists()) {
            logger.info("Generate Database in plugin folder (" + database.getName() + ")");
            javaPlugin.saveResource(database.getName(), false);
        }
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) connection = DriverManager.getConnection(databaseLink);
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }
}