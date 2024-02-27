package net.lyflow.skyblock.event.island.upgrade;

import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import org.bukkit.entity.Player;

public abstract class IslandUpgradeLeveledEvent extends IslandUpgradeEvent {

    protected int levelTo;
    protected int levelFrom;

    protected IslandUpgradeLeveledEvent(Player player, IslandUpgrade islandUpgrade, int levelFrom, int levelTo) {
        super(player, islandUpgrade);
        this.levelTo = levelTo;
        this.levelFrom = levelFrom;
    }

    public int getLevelTo() {
        return levelTo;
    }

    public int getLevelFrom() {
        return levelFrom;
    }
}
