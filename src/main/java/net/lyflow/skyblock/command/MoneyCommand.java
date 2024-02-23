package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.utils.CommandUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

public class MoneyCommand implements CommandExecutor, TabCompleter {

    private static final String[] fArgsCompletion = new String[]{"send", "value"};

    private final SkyBlock skyblock;

    public MoneyCommand(SkyBlock skyblock) {
        this.skyblock = skyblock;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof final Player player) {
            if (args.length == 0 || args[0].equalsIgnoreCase("value")) {
                try {
                    player.sendMessage("§aVous avez " + new AccountRequest(skyblock.getDatabase(), true).getMoney(player.getUniqueId()) + "$");
                } catch (SQLException e) {
                    skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                }
                return true;
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("send")) {
                    if (args[1].equals(player.getName())) {
                        player.sendMessage("§cVous ne pouvez pas vous envoyer de l'argent");
                        return true;
                    }
                    try {
                        final float amount = Math.round(Float.parseFloat(args[2]) * 100.0) / 100.0f;
                        if (amount <= 0) {
                            player.sendMessage("§cVous ne pouvez pas envoyer moins de 0$");
                            return true;
                        }

                        final AccountRequest accountRequest = new AccountRequest(skyblock.getDatabase(), false);
                        final float playerMoney = accountRequest.getMoney(player.getUniqueId());
                        if (playerMoney < amount) {
                            skyblock.getDatabase().closeConnection();
                            player.sendMessage("§cVous n'avez pas " + amount + "$");
                            return true;
                        }

                        final OfflinePlayer offlinePlayer = accountRequest.getOfflinePlayerByName(args[1]);
                        if (offlinePlayer == null) {
                            skyblock.getDatabase().closeConnection();
                            player.sendMessage("§cLe joueur " + args[1] + " n'existe pas");
                            return true;
                        }

                        accountRequest.setMoney(player.getUniqueId(), accountRequest.getMoney(player.getUniqueId()) - amount);
                        accountRequest.setMoney(offlinePlayer.getUniqueId(), accountRequest.getMoney(offlinePlayer.getUniqueId()) + amount);
                        if (offlinePlayer.isOnline())
                            offlinePlayer.getPlayer().sendMessage("§aVous avez reçu " + amount + " $ de " + player.getName());
                        player.sendMessage("§aVous avez envoyé " + amount + "$ à " + offlinePlayer.getName());
                        skyblock.getDatabase().closeConnection();
                    } catch (SQLException e) {
                        skyblock.getLogger().log(Level.SEVERE, e.getMessage(), e);
                    } catch (NumberFormatException e) {
                        player.sendMessage("§c" + args[2] + " n'est pas un nombre valide");
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (commandSender instanceof Player) {
            if (args.length == 1) {
                return CommandUtils.completionTable(args[0], fArgsCompletion);
            }
        }
        return null;
    }

}
