package net.quillcraft.skyblock.command;

import net.quillcraft.skyblock.inventory.challenge.ChallengeInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChallengeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (commandSender instanceof final Player player) {
            player.openInventory(ChallengeInventory.getMenuChallengeInventory());
            return true;
        }
        return false;
    }
}
