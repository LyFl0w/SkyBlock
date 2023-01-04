package net.lyflow.skyblock;

import net.lyflow.skyblock.database.Database;

import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.manager.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class SkyBlock extends JavaPlugin {

    private static SkyBlock INSTANCE;

    private Database database;

    private IslandUpgradeManager islandUpgradeManager;
    private ChallengeManager challengeManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.database = new Database(this, "skyblock.db");

        // INIT MANAGER
        new PluginManager(this);

        this.islandUpgradeManager = new IslandUpgradeManager(this);

        this.challengeManager = new ChallengeManager(this);
        this.challengeManager.init();
    }

    @Override
    public void onDisable() {
        try {
            database.closeConnection();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public IslandUpgradeManager getIslandUpgradeManager() {
        return islandUpgradeManager;
    }

    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    public Database getDatabase() {
        return database;
    }

    public static SkyBlock getInstance() {
        return INSTANCE;
    }
}