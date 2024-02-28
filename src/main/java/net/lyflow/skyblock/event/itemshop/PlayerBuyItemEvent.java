package net.lyflow.skyblock.event.itemshop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.shop.ItemShop;
import net.lyflow.skyblock.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.ArrayList;


public class PlayerBuyItemEvent extends ShopEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    public PlayerBuyItemEvent(SkyBlock skyblock, Player player, ItemShop itemShop, int amount) {
        super(skyblock, player, itemShop, amount);

        if (amount <= 0) {
            player.sendMessage("Le nombre d'item sélectionné doit être suppérieur à 0");
            setCancelled(true);
            return;
        }
        final AccountRequest accountRequest = new AccountRequest(skyblock.getDatabase(), false);

        try {
            final float playerMoney = accountRequest.getMoney(player.getUniqueId());
            final float price = itemShop.getBuyPrice() * amount;
            final ItemStack itemStack = new ItemStack(itemShop.getMaterial());
            final String formatedItemStackName = StringUtils.capitalizeSentence(itemStack.getType().name(), "_", " ");

            if (playerMoney < price) {
                skyblock.getDatabase().closeConnection();
                player.sendMessage("§cVous n'avez pas assez de money pour acheter " + amount + " " + formatedItemStackName);
                setCancelled(true);

                skyblock.getDatabase().closeConnection();
                return;
            }
            accountRequest.setMoney(player.getUniqueId(), playerMoney - price);
            skyblock.getDatabase().closeConnection();

            final PlayerInventory inventory = player.getInventory();
            final int stackNumber = amount / itemStack.getMaxStackSize();
            final int rest = amount % itemStack.getMaxStackSize();
            final ItemStack stack = new ItemStack(itemStack.getType(), itemStack.getMaxStackSize());

            final ArrayList<ItemStack> dropItems = new ArrayList<>();

            for (int i = 0; i < stackNumber; i += 1) {
                dropItems.addAll(inventory.addItem(stack).values());
            }
            if (rest > 0) dropItems.addAll(inventory.addItem(new ItemStack(itemStack.getType(), rest)).values());

            final Location location = player.getLocation();
            dropItems.forEach(item -> player.getWorld().dropItemNaturally(location, item));

            player.sendMessage("§aVous avez acheté " + amount + " " + formatedItemStackName);
        } catch (SQLException e) {
            throw new IllegalCallerException(e);
        }

    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    @NotNull
    public HandlerList getHandlers() {
        return getHandlerList();
    }

}
