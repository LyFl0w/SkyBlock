package net.lyflow.skyblock;

import net.lyflow.skyblock.database.Database;

import net.lyflow.skyblock.manager.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class SkyBlock extends JavaPlugin {

    private static SkyBlock INSTANCE;
    private Database database;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.database = new Database(this, "skyblock.db");

        new PluginManager(this);
    }

    @Override
    public void onDisable() {
        try {
            database.closeConnection();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Database getDatabase() {
        return database;
    }

    public static SkyBlock getInstance() {
        return INSTANCE;
    }
}