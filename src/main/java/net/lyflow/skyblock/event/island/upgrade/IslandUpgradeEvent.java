package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class IslandUpgradeEvent extends Event implements Cancellable {

    private final Player player;
    private final IslandUpgrade islandUpgrade;

    protected boolean isCancelled = false;

    protected IslandUpgradeEvent(Player player, IslandUpgrade islandUpgrade) {
        this.player = player;
        this.islandUpgrade = islandUpgrade;
    }

    public final Player getPlayer() {
        return player;
    }

    public IslandUpgrade getIslandUpgrade() {
        return islandUpgrade;
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