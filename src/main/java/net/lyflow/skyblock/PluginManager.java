package net.lyflow.skyblock;

import net.lyflow.skyblock.command.IslandCommand;
import net.lyflow.skyblock.command.LobbyCommand;
import net.lyflow.skyblock.command.MoneyCommand;
import net.lyflow.skyblock.command.ShopCommand;
import net.lyflow.skyblock.listener.block.BlockSpreadListener;
import net.lyflow.skyblock.listener.inventory.InventoryClickListener;
import net.lyflow.skyblock.listener.player.AsyncPlayerPreLoginListener;
import net.lyflow.skyblock.listener.player.PlayerJoinListener;
import net.lyflow.skyblock.listener.player.PlayerQuitListener;
import net.lyflow.skyblock.listener.player.PlayerToggleSneakListener;

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
        pluginManager.registerEvents(new PlayerQuitListener(skyblock), skyblock);
        pluginManager.registerEvents(new PlayerJoinListener(skyblock), skyblock);
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(skyblock), skyblock);
        pluginManager.registerEvents(new PlayerToggleSneakListener(), skyblock);

        // BLOCK EVENT
        pluginManager.registerEvents(new BlockSpreadListener(), skyblock);


        // INVENTORY EVENT
        pluginManager.registerEvents(new InventoryClickListener(skyblock), skyblock);
    }

    private void registerCommands() {
        final IslandCommand islandCommand = new IslandCommand(skyblock);
        final PluginCommand islandPluginCommand = skyblock.getCommand("island");
        islandPluginCommand.setExecutor(islandCommand);
        islandPluginCommand.setTabCompleter(islandCommand);

        skyblock.getCommand("money").setExecutor(new MoneyCommand(skyblock));

        skyblock.getCommand("lobby").setExecutor(new LobbyCommand(skyblock));
        skyblock.getCommand("shop").setExecutor(new ShopCommand());
    }
}