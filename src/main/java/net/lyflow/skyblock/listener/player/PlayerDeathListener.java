package net.lyflow.skyblock.listener.player;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerDeathListener implements Listener {

    private final SkyBlock skyblock;

    public PlayerDeathListener(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final UUID uuid = player.getUniqueId();
        final AccountRequest accountRequest = new AccountRequest(skyblock.getDatabase(), false);

        try {
            final float money = accountRequest.getMoney(uuid);
            final float toRemove = (money > 150) ? money * 0.01f : 15;
            accountRequest.setMoney(uuid, money - toRemove);
            skyblock.getDatabase().closeConnection();

            player.sendMessage("§cVous avez perdu " + toRemove + "$");
        } catch (SQLException e) {
            skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

}
