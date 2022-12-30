package net.lyflow.skyblock.event.itemshop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.utils.InventoryUtils;
import net.lyflow.skyblock.utils.StringUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class PlayerSellItemEvent extends ShopEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerSellItemEvent(SkyBlock skyblock, Player player, ItemShop itemShop, int amount) {
        super(skyblock, player, itemShop, amount);

        if(amount <= 0) {
            player.sendMessage("Le nombre d'item sélectionné doit être suppérieur à 0");
            return;
        }

        final AccountRequest accountRequest = new AccountRequest(skyBlock.getDatabase(), false);

        try {
            final ItemStack itemStack = new ItemStack(itemShop.getMaterial(), amount);
            final String formatedItemStackName = StringUtils.capitalizeSentence(itemStack.getType().name(), "_", " ");

            if(!player.getInventory().contains(itemShop.getMaterial(), amount)) {
                player.sendMessage("§cVous n'avez pas "+amount+" "+formatedItemStackName+ " à vendre");
                setCancelled(true);
                return;
            }
            accountRequest.setMoney(player.getUniqueId(), accountRequest.getMoney(player.getUniqueId())+itemShop.getSellPrice() * amount);
            skyblock.getDatabase().closeConnection();

            InventoryUtils.removeItems(player, itemShop.getMaterial(), amount);
            player.sendMessage("§aVous avez vendu "+amount+" "+formatedItemStackName);
        } catch(Exception e) {
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
