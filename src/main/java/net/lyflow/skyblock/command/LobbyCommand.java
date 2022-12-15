package net.lyflow.skyblock.command;

import net.lyflow.skyblock.SkyBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LobbyCommand implements CommandExecutor {

    public final static Location spawn = Bukkit.getServer().getWorld("lobby").getSpawnLocation().add(0.5, 0, 0.5);

    private final SkyBlock skyBlock;
    public LobbyCommand(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(commandSender instanceof final Player player) {
            if(args.length == 0){
                player.sendMessage("§aTéléportation au §2Lobby");
                player.teleport(spawn);
                return true;
            }
            player.sendMessage(command.getUsage());
            return true;
        }
        return false;
    }
}
