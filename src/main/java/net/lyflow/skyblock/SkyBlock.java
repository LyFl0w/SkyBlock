package net.lyflow.skyblock;

import net.lyflow.skyblock.database.Database;

import org.bukkit.plugin.java.JavaPlugin;

public class SkyBlock extends JavaPlugin {

    private static SkyBlock INSTANCE;
    private Database database;

    @Override
    public void onEnable() {
        this.INSTANCE = this;

        this.database = new Database(this, "skyblock.db");

        new PluginManager(this);
    }

    @Override
    public void onDisable() {

    }

    public Database getDatabase() {
        return database;
    }

    public static SkyBlock getInstance() {
        return INSTANCE;
    }
}