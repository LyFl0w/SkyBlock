package net.lyflow.skyblock;

import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.manager.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class SkyBlock extends JavaPlugin {

    private static SkyBlock instance;

    private Database database;

    private ChallengeManager challengeManager;

    public static SkyBlock getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.database = new Database(this, "skyblock.db");

        new PluginManager(this);
        challengeManager = new ChallengeManager(this);
        challengeManager.init();
    }

    @Override
    public void onDisable() {
        try {
            database.closeConnection();
        } catch (SQLException e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    public Database getDatabase() {
        return database;
    }
}