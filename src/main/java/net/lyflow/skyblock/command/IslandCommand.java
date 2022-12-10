package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.utils.CommandUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class IslandCommand implements CommandExecutor, TabCompleter {

    private final static String[] argsCompletion = new String[]{"create", "delete", "config", "invite", "kick", "setowner"};

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
                if (sub.equalsIgnoreCase(argsCompletion[0])) {
                    player.openInventory(IslandDifficulty.openInventoryDifficulty());
                    return true;
                }

                // DELETE
                if (sub.equalsIgnoreCase(argsCompletion[1])) {

                    return true;
                }

                // CONFIG
                if (sub.equalsIgnoreCase(argsCompletion[2])) {

                    return true;
                }
                return false;
            }

            if (args.length == 2) {
                // INVITE
                if (sub.equalsIgnoreCase(argsCompletion[3])) {

                    return true;
                }

                // KICK
                if (sub.equalsIgnoreCase(argsCompletion[4])) {

                    return true;
                }

                // SET OWNER
                if (sub.equalsIgnoreCase(argsCompletion[5])) {

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
