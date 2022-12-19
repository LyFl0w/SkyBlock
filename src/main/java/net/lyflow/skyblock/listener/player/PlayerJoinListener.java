package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.command.LobbyCommand;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.database.request.island.IslandRequest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.*;

public class PlayerJoinListener implements Listener {

    private final SkyBlock skyBlock;
    public PlayerJoinListener(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        final Player player = event.getPlayer();

        final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);
        if(!accountRequest.hasAccount(player.getUniqueId())) {
            player.teleport(LobbyCommand.spawn);
            accountRequest.createPlayerAccount(player);

            event.setJoinMessage("§6Bienvenue à §b"+player.getName()+" §6!");
        } else {
            accountRequest.updatePlayerName(player);
            if(!new IslandRequest(skyBlock.getDatabase(), false).hasIsland(player.getUniqueId()))
                player.teleport(LobbyCommand.spawn);
        }

        skyBlock.getDatabase().closeConnection();
    }

}