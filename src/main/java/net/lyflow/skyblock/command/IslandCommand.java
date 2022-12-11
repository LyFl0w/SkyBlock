package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.island.IslandMate;
import net.lyflow.skyblock.island.MateStatus;
import net.lyflow.skyblock.island.PlayerIslandRequest;
import net.lyflow.skyblock.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class IslandCommand implements CommandExecutor, TabCompleter {

    private final static String[] argsCompletion = new String[]{"create", "delete", "config", "list", "invite", "kick", "setowner"};

    private final SkyBlock skyBlock;
    public IslandCommand(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if(args.length == 0) return false;
        if (commandSender instanceof final Player player) {
            final String sub = args[0];
            if (args.length == 1) {
                // CREATE
                if (sub.equalsIgnoreCase("create")) {
                    player.openInventory(IslandDifficulty.openInventoryDifficulty());
                    return true;
                }

                // DELETE
                if (sub.equalsIgnoreCase("delete")) {

                    return true;
                }

                // CONFIG
                if (sub.equalsIgnoreCase("config")) {

                    return true;
                }

                // LIST
                if (sub.equalsIgnoreCase("list")) {
                    final PlayerIslandRequest playerIslandRequest = new PlayerIslandRequest(skyBlock.getDatabase(), false);

                    try {
                        if(!playerIslandRequest.playerHasIsland(player)) {
                            player.sendMessage("§cVous n'avez pas d'île, créer vous en une avec la commande §6/island create");
                            return true;
                        }
                        final List<IslandMate> islandMates = playerIslandRequest.islandMates(player);

                        skyBlock.getDatabase().closeConnection();

                        final IslandMate owner = islandMates.stream().parallel().filter(islandMate ->
                                islandMate.status() == MateStatus.OWNER).findFirst().get();

                        final StringBuilder messageBuilder = new StringBuilder("List des joueurs de votre île\n").append(owner.getStatus())
                                .append(" ").append(owner.player().getName());

                        islandMates.remove(owner);

                        islandMates.stream().sorted(Comparator.comparingInt(IslandMate::isOnline)).forEach(islandMate ->
                                messageBuilder.append("\n").append(islandMate.getStatus()).append(" ").append(islandMate.player().getName()));

                        player.sendMessage(messageBuilder.toString());
                    } catch(SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                }
                return false;
            }

            if (args.length == 2) {
                // INVITE
                if (sub.equalsIgnoreCase("invite")) {

                    return true;
                }

                // KICK
                if (sub.equalsIgnoreCase("kick")) {

                    return true;
                }

                // SET OWNER
                if (sub.equalsIgnoreCase("setowner")) {

                    return true;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender commandSender, @Nonnull Command command, @Nonnull String label, String[] args) {
        if (commandSender instanceof Player) {
            if (args.length == 1 && args[0] != null) {
                return CommandUtils.completionTable(args[0], argsCompletion);
            }
        }
        return null;
    }
}
