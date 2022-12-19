package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.text.DecimalFormat;

public class MoneyCommand implements CommandExecutor{

    private final SkyBlock skyBlock;
    public MoneyCommand(SkyBlock skyblock) {
        this.skyBlock = skyblock;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(commandSender instanceof final Player player) {
            if(args.length == 0) {
                try {
                    player.sendMessage("§aVous avez "+new AccountRequest(skyBlock.getDatabase(), true).getMoney(player.getUniqueId())+"$");
                } catch(SQLException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("send")){
                    final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);
                    try {
                        final float amount = Float.parseFloat(new DecimalFormat("0.00").format(Float.parseFloat(args[2])));
                        final OfflinePlayer offlinePlayer = accountRequest.getOfflinePlayerByName(args[1]);
                        if(offlinePlayer == null) {
                            player.sendMessage("§cLe joueur "+args[1]+" n'existe pas");
                            return true;
                        }
                        accountRequest.setMoney(player.getUniqueId(), accountRequest.getMoney(player.getUniqueId()) - amount);
                        accountRequest.setMoney(offlinePlayer.getUniqueId(), accountRequest.getMoney(offlinePlayer.getUniqueId()) + amount);
                        if(offlinePlayer.isOnline()) offlinePlayer.getPlayer().sendMessage("§aVous avez reçu "+amount+" $ de "+player.getName());
                        player.sendMessage("§aVous avez envoyé "+amount+"$ à "+offlinePlayer.getName());
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    } catch(NumberFormatException e){
                        player.sendMessage("§c"+args[2]+" n'est pas un nombre valide");
                    }
                    return true;
                }
            }
        }
        return false;
    }

}
