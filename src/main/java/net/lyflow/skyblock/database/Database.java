package net.lyflow.skyblock.database;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Database {

    private JavaPlugin javaPlugin;
    private final Logger logger;

    private final String databaseLink;
    private Connection connection;

    public Database(JavaPlugin javaPlugin, String databaseName) {
        this.javaPlugin = javaPlugin;
        this.logger = javaPlugin.getLogger();

        final File database = new File(javaPlugin.getDataFolder(), databaseName);
        this.databaseLink = "jdbc:sqlite:"+database.getPath();

        initDatabase(database);
    }

    private void initDatabase(File database) {

        // Generate database if not exist
        if(!database.exists()) {
            logger.info("Generate Database in plugin folder ("+database.getName()+")");
            try {
                FileUtils.copyInputStreamToFile(javaPlugin.getResource(database.getName()), database);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()){
            logger.info("Init connexion to Database");
            connection = DriverManager.getConnection(databaseLink);
        }
        return connection;
    }
}