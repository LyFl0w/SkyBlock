package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.request.account.AccountRequest;

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
            accountRequest.createPlayerAccount(player);
            skyBlock.getDatabase().closeConnection();
            event.setJoinMessage("§6Bienvenue à §b"+player.getName()+" §6!");
        } else {
            accountRequest.updatePlayerName(player);
        }
    }

}