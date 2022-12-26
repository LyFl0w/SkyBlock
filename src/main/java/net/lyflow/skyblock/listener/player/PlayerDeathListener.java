package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerDeathListener implements Listener {

    private final SkyBlock skyBlock;
    public PlayerDeathListener(SkyBlock skyblock) {
        this.skyBlock = skyblock;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final UUID uuid = player.getUniqueId();
        final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);

        try {

            final float money = accountRequest.getMoney(uuid);
            final float toRemove = money*0.05f;
            accountRequest.setMoney(uuid, money-toRemove);
            skyBlock.getDatabase().closeConnection();

            player.sendMessage("§cVous avez perdu "+toRemove+"$");
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
