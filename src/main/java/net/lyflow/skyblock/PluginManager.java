package net.lyflow.skyblock;

import net.lyflow.skyblock.command.IslandCommand;
import net.lyflow.skyblock.listener.inventory.InventoryClickListener;
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

        // PLAYER EVENT
        pluginManager.registerEvents(new PlayerJoinLister(skyblock), skyblock);

        // INVENTORY EVENT
        pluginManager.registerEvents(new InventoryClickListener(skyblock), skyblock);
    }

    private void registerCommands() {
        final IslandCommand islandCommand = new IslandCommand(skyblock);
        final PluginCommand islandPluginCommand = skyblock.getCommand("island");
        islandPluginCommand.setExecutor(islandCommand);
        islandPluginCommand.setTabCompleter(islandCommand);
    }
}