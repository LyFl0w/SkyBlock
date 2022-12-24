package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
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

    public PluginManager(SkyBlock skyBlock) {
        registerEvents(skyBlock, skyBlock.getServer().getPluginManager());
        registerCommands(skyBlock);
    }

    private void registerEvents(SkyBlock skyBlock, org.bukkit.plugin.PluginManager pluginManager) {
        // PLAYER EVENT
        pluginManager.registerEvents(new PlayerQuitListener(skyBlock), skyBlock);
        pluginManager.registerEvents(new PlayerJoinListener(skyBlock), skyBlock);
        pluginManager.registerEvents(new AsyncPlayerPreLoginListener(skyBlock), skyBlock);
        pluginManager.registerEvents(new PlayerToggleSneakListener(), skyBlock);

        // BLOCK EVENT
        pluginManager.registerEvents(new BlockSpreadListener(), skyBlock);

        // INVENTORY EVENT
        pluginManager.registerEvents(new InventoryClickListener(skyBlock), skyBlock);
        //pluginManager.registerEvents(new CraftItemListener(skyBlock), skyBlock);
    }

    private void registerCommands(SkyBlock skyBlock) {
        final IslandCommand islandCommand = new IslandCommand(skyBlock);
        final PluginCommand islandPluginCommand = skyBlock.getCommand("island");
        islandPluginCommand.setExecutor(islandCommand);
        islandPluginCommand.setTabCompleter(islandCommand);

        skyBlock.getCommand("money").setExecutor(new MoneyCommand(skyBlock));

        skyBlock.getCommand("lobby").setExecutor(new LobbyCommand(skyBlock));
        skyBlock.getCommand("shop").setExecutor(new ShopCommand());
    }
}