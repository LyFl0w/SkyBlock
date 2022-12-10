package net.lyflow.skyblock;


import net.lyflow.skyblock.command.IslandCommand;
import net.lyflow.skyblock.listener.player.PlayerJoinLister;
import org.bukkit.command.PluginCommand;

public class PluginManager {

    private final SkyBlock skyblock;

    public PluginManager(SkyBlock skyBlock) {
        this.skyblock = skyBlock;

        registerEvents(skyBlock.getServer().getPluginManager());
        registerCommands();
    }

    private void registerEvents(org.bukkit.plugin.PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerJoinLister(skyblock), skyblock);
    }

    private void registerCommands() {
        final IslandCommand islandCommand = new IslandCommand(skyblock);
        final PluginCommand islandPluginCommand = skyblock.getCommand("island");
        islandPluginCommand.setExecutor(islandCommand);
        islandPluginCommand.setTabCompleter(islandCommand);
    }
}