package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.command.*;
import net.lyflow.skyblock.listener.inventory.InventoryClickListener;
import net.lyflow.skyblock.listener.player.*;

import org.bukkit.command.PluginCommand;

public class PluginManager {

    public PluginManager(SkyBlock skyblock) {
        registerEvents(skyblock, skyblock.getServer().getPluginManager());
        registerCommands(skyblock);
    }

    private void registerEvents(SkyBlock skyblock, org.bukkit.plugin.PluginManager pluginManager) {
        // PLAYER EVENT
        pluginManager.registerEvents(new PlayerQuitListener(skyblock), skyblock);
        pluginManager.registerEvents(new PlayerJoinListener(skyblock), skyblock);
        pluginManager.registerEvents(new PlayerDeathListener(skyblock), skyblock);
        pluginManager.registerEvents(new PlayerRespawnListener(skyblock), skyblock);

        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(skyblock), skyblock);
        pluginManager.registerEvents(new PlayerToggleSneakListener(), skyblock);

        // BLOCK EVENT

        // INVENTORY EVENT
        pluginManager.registerEvents(new InventoryClickListener(skyblock), skyblock);
        //pluginManager.registerEvents(new CraftItemListener(skyblock), skyblock);
    }

    private void registerCommands(SkyBlock skyblock) {
        final IslandCommand islandCommand = new IslandCommand(skyblock);
        final PluginCommand islandPluginCommand = skyblock.getCommand("island");
        islandPluginCommand.setExecutor(islandCommand);
        islandPluginCommand.setTabCompleter(islandCommand);

        skyblock.getCommand("money").setExecutor(new MoneyCommand(skyblock));

        skyblock.getCommand("lobby").setExecutor(new LobbyCommand(skyblock));
        skyblock.getCommand("shop").setExecutor(new ShopCommand());
        skyblock.getCommand("challenge").setExecutor(new ChallengeCommand());


    }
}