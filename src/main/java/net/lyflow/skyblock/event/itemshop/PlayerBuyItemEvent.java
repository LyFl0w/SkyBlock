package net.lyflow.skyblock.event.itemshop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.shop.ItemShop;

import net.lyflow.skyblock.utils.StringUtils;
import org.bukkit.craftbukkit.v1_19_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PlayerBuyItemEvent extends ShopEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerBuyItemEvent(SkyBlock skyblock, Player player, ItemShop itemShop, int amount) {
        super(skyblock, player, itemShop, amount);

        final AccountRequest accountRequest = new AccountRequest(skyblock.getDatabase(), false);
        try {
            final float playerMoney = accountRequest.getMoney(player.getUniqueId());
            final float price = itemShop.getBuyPrice() * amount;
            final ItemStack itemStack = new ItemStack(itemShop.getMaterial(), amount);
            final String formatedItemStackName = StringUtils.capitalizeSentence(itemStack.getType().name(), "_", " ");

            if(playerMoney < price) {
                skyblock.getDatabase().closeConnection();
                player.sendMessage("§cVous n'avez pas assez de money pour acheter "+amount+" "+formatedItemStackName);
                setCancelled(true);
                return;
            }
            accountRequest.setMoney(player.getUniqueId(), playerMoney-price);
            skyblock.getDatabase().closeConnection();

            player.getInventory().addItem(itemStack);
            player.sendMessage("§aVous avez acheté "+amount+" "+formatedItemStackName);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
