package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.*;

public class PlayerJoinLister implements Listener {

    private final SkyBlock skyBlock;
    public PlayerJoinLister(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        final Player player = event.getPlayer();
        final Connection connection = skyBlock.getDatabase().getConnection();
        final PreparedStatement preparableStatement = connection.prepareStatement("SELECT COUNT(*) FROM Player WHERE UUID = ?");

        preparableStatement.setString(1, player.getUniqueId().toString());

        // Check if player has an account
        final boolean hasAccount = (0 != preparableStatement.executeQuery().getInt(1));

        if(!hasAccount) {
            final PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO Player (UUID) VALUES (?)");
            preparedStatement2.setString(1, player.getUniqueId().toString());
            preparedStatement2.execute();

            skyBlock.getLogger().info("Account create for : "+player.getName());

            event.setJoinMessage("§6Bienvenue à §b"+player.getName()+" §6!");
        }

        connection.close();
    }

}