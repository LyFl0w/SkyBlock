package net.lyflow.skyblock;

import org.bukkit.plugin.java.JavaPlugin;

public class SkyBlock extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Skyblock plugin is on");
    }

    @Override
    public void onDisable() {
        getLogger().info("Skyblock plugin is off");
    }
}