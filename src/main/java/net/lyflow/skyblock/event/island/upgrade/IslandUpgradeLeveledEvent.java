package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import org.bukkit.entity.Player;

public abstract class IslandUpgradeLeveledEvent extends IslandUpgradeEvent {

    protected int level;

    protected IslandUpgradeLeveledEvent(Player player, IslandUpgrade islandUpgrade, int level) {
        super(player, islandUpgrade);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
