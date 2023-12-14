package net.quillcraft.skyblock.event.itemshop;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.shop.ItemShop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class ShopEvent extends Event implements Cancellable {

    protected final SkyBlock skyblock;

    protected final Player player;
    protected final ItemShop itemShop;
    protected final int amount;

    protected boolean isCancelled = false;

    public ShopEvent(SkyBlock skyblock, Player player, ItemShop itemShop, int amount) {
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
