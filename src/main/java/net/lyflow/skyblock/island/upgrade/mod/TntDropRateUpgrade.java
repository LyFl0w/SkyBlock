package net.lyflow.skyblock.island.upgrade.mod;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import net.lyflow.skyblock.island.upgrade.LevelUpgradeKey;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.LocationUtils;
import net.lyflow.skyblock.utils.iteminfo.UniqueItemInfo;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.Map;


public class TntDropRateUpgrade extends IslandUpgrade {

    private final double defaultRate;

    public TntDropRateUpgrade(SkyBlock skyBlock, int id, List<LevelUpgrade> levelUpgrades, Map<String, Object> data, UniqueItemInfo itemInfo) {
        super(skyBlock, id, IslandUpgrade.Type.TNT_DROP_RATE, levelUpgrades, itemInfo);
        this.defaultRate = LevelUpgradeKey.DROP_RATE.getData(data);
    }

    public static class ListenerEvent implements Listener {

        private final IslandUpgradeManager islandUpgradeManager;

        public ListenerEvent(IslandUpgradeManager islandUpgradeManager) {
            this.islandUpgradeManager = islandUpgradeManager;
        }

        @EventHandler
        public void onEntityExplode(EntityExplodeEvent event) {
            event.setYield(getDefaultYield(event.getLocation(), event.getYield()));
        }

        @EventHandler
        public void onBlockExplode(BlockExplodeEvent event) {
            event.setYield(getDefaultYield(event.getBlock().getLocation(), event.getYield()));
        }

        private float getDefaultYield(Location location, float defaultYield) {
            final String worldName = location.getWorld().getName();

            if (worldName.startsWith("skyblock-map")) {
                final TntDropRateUpgrade islandUpgrade = (TntDropRateUpgrade) islandUpgradeManager
                        .getUniqueIslandUpgradesByType(IslandUpgrade.Type.TNT_DROP_RATE);
                if (islandUpgrade == null) return defaultYield;

                final int islandID = LocationUtils.getIslandID(worldName);
                final IslandUpgradeStatus upgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);
                final LevelUpgrade tntDropRateLevelUpgrade = islandUpgrade.levelUpgradeManager.getLevel(upgradeStatus.getCurrentLevel());
                return upgradeStatus.isEnable()
                        ? tntDropRateLevelUpgrade.getData(LevelUpgradeKey.DROP_RATE)
                        : (float) islandUpgrade.defaultRate;
            }

            return defaultYield;
        }
    }

}
