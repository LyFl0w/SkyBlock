package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.shop.ItemShop;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;


public abstract class IslandUpgradeEvent extends Event implements Cancellable {

    protected final SkyBlock skyblock;

    protected final Player player;
    protected final ItemShop itemShop;
    protected final int amount;

    protected boolean isCancelled = false;

    protected IslandUpgradeEvent(SkyBlock skyblock, Player player, ItemShop itemShop, int amount) {
        this.skyblock = skyblock;
        this.player = player;
        this.itemShop = itemShop;
        this.amount = amount;
    }

    public final int getAmount() {
        return amount;
    }

    public final ItemShop getItemShop() {
        return itemShop;
    }

    public final Player getPlayer() {
        return player;
    }

    @Override
    public final boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public final void setCancelled(boolean setCancelled) {
        this.isCancelled = setCancelled;
    }

}