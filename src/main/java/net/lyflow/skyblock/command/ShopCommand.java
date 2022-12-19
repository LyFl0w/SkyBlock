package net.lyflow.skyblock.command;

import net.lyflow.skyblock.inventory.shop.ShopInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0 && commandSender instanceof final Player player) {
            player.openInventory(ShopInventory.getShopMenuInventory());
            return true;
        }
        return false;
    }
}
