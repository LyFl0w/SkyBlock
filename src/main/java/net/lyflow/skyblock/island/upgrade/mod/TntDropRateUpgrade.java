package net.lyflow.skyblock.island.upgrade.mod;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.ArrayUtils;
import net.lyflow.skyblock.utils.LocationUtils;
import net.lyflow.skyblock.utils.iteminfo.UniqueItemInfo;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;
import java.util.Set;

public class TntDropRateUpgrade extends IslandUpgrade {

    private final float defaultRate;

    public TntDropRateUpgrade(SkyBlock skyBlock, int id, float defaultRate, TntDropRateLevelUpgrade levelUpgrade, UniqueItemInfo itemInfo) {
        super(skyBlock, id, IslandUpgrade.Type.TNT_DROP_RATE, levelUpgrade, itemInfo);

        this.defaultRate = defaultRate;
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

                return upgradeStatus.isEnable()
                        ? ((TntDropRateLevelUpgrade) islandUpgrade.levelUpgrade).getDropRates(upgradeStatus.getCurrentLevel())
                        : islandUpgrade.defaultRate;
            }

            return defaultYield;
        }
    }

    public static class TntDropRateLevelUpgrade extends LevelUpgrade {

        private final float[] dropRates;

        public TntDropRateLevelUpgrade(List<Float> prices, Set<Integer> slot, List<Float> dropRates, List<List<String>> description) {
            super(prices, slot, description);

            if (dropRates.size() != prices.size())
                throw new IllegalArgumentException("There must be as many drop rates as upgrade levels !");

            this.dropRates = ArrayUtils.toFloatArray(dropRates);
        }

        public float getDropRates(int level) {
            if (level < 1 || level > dropRates.length)
                throw new IllegalArgumentException("Drop rate for " + level + " doesn't exist !");

            return dropRates[level - 1];
        }

    }
}
