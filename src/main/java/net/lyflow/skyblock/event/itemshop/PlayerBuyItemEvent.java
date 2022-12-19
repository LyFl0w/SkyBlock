package net.lyflow.skyblock.event.itemshop;

import net.lyflow.skyblock.shop.ItemShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerBuyItemEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled = false;
    private final ItemShop itemShop;

    public PlayerBuyItemEvent(Player who, ItemShop itemShop) {
        super(who);
        this.itemShop = itemShop;
    }

    public ItemShop getItemShop() {
        return itemShop;
    }

    @Override @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean setCancelled) {
        this.isCancelled = setCancelled;
    }
}
