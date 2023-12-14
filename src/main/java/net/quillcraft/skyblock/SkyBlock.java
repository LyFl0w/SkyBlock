package net.quillcraft.skyblock;

import net.quillcraft.skyblock.database.Database;
import net.quillcraft.skyblock.manager.ChallengeManager;
import net.quillcraft.skyblock.manager.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class SkyBlock extends JavaPlugin {

    private static SkyBlock INSTANCE;

    private net.quillcraft.skyblock.database.Database database;

    private ChallengeManager challengeManager;

    public static SkyBlock getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

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
            throw new RuntimeException(e);
        }
    }

    public ChallengeManager getChallengeManager() {
        return challengeManager;
    }

    public Database getDatabase() {
        return database;
    }
}