package net.lyflow.skyblock.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import net.lyflow.skyblock.island.upgrade.LevelUpgradeKey;
import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;
import net.lyflow.skyblock.island.upgrade.mod.TntDropRateUpgrade;
import net.lyflow.skyblock.loader.gson.EmptyListToNullFactory;
import net.lyflow.skyblock.loader.island.upgrade.IslandUpgradeData;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IslandUpgradeManager {

    private final List<IslandUpgrade> islandUpgrades;

    public IslandUpgradeManager() {
        islandUpgrades = new ArrayList<>();
    }

    public void init(SkyBlock skyblock) {
        registerIslandUpgrades(skyblock);
        registerUpgradeEvent(skyblock, skyblock.getServer().getPluginManager());
    }

    private void registerIslandUpgrades(SkyBlock skyblock) {

        final Gson gson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapterFactory(EmptyListToNullFactory.INSTANCE)
                .setPrettyPrinting()
                .create();

        final IslandUpgradeData upgrade = new IslandUpgradeData(4, 4, "tnt_drop_rate",
                "minecraft:tnt", "Tnt Drop Rate Upgrade", List.of(),
                List.of(
                        new LevelUpgrade(1_000f, 1, List.of("Vous en avez marre des 50% ?", "En voilà 60% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.6f)),
                        new LevelUpgrade(2_000f, 2, List.of("Vous en avez marre des 60% ?", "En voilà 80% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.8f)),
                        new LevelUpgrade(3_000f, 3, List.of("Coquin !!!", "En voilà 100% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 1.0f))
                ),
                Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.5f)
        );

        final String json = gson.toJson(upgrade);
        System.out.println(json);

        System.out.println("|-|".repeat(20));

        final IslandUpgradeData upgradeDeserialize = gson.fromJson(json, IslandUpgradeData.class);
        System.out.println(gson.toJson(upgradeDeserialize));

        try {
            addIslandUpgrade(upgradeDeserialize.toUpgrade(skyblock));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void registerUpgradeEvent(SkyBlock skyblock, PluginManager pluginManager) {
        pluginManager.registerEvents(new CobblestoneGeneratorUpgrade.ListenerEvent(this), skyblock);
        pluginManager.registerEvents(new TntDropRateUpgrade.ListenerEvent(this), skyblock);
    }

    private void addIslandUpgrade(IslandUpgrade islandUpgrade) {
        this.islandUpgrades.add(islandUpgrade);
    }

    public List<IslandUpgrade> getIslandUpgrades() {
        return islandUpgrades;
    }

    @Nullable
    public IslandUpgrade getIslandUpgradesByID(int id) {
        for (final IslandUpgrade islandUpgrade : islandUpgrades) {
            if (islandUpgrade.getID() == id) return islandUpgrade;
        }
        return null;
    }

    @Nullable
    public IslandUpgrade getUniqueIslandUpgradesByType(IslandUpgrade.Type type) {
        if (!type.isUniqueEvent()) throw new IllegalArgumentException("Type " + type.name() + " is not unique !");
        for (final IslandUpgrade islandUpgrade : islandUpgrades) {
            if (islandUpgrade.getType() == type) return islandUpgrade;
        }
        return null;
    }

    @Nullable
    public IslandUpgrade getIslandUpgradesBySlot(IslandUpgrade.Type type, int slot) {
        for (final IslandUpgrade islandUpgrade : islandUpgrades) {
            if (islandUpgrade.getSlot() == slot && islandUpgrade.getType() == type)
                return islandUpgrade;
        }
        return null;
    }

    public List<IslandUpgrade> getIslandUpgradesByType(IslandUpgrade.Type type) {
        return islandUpgrades.stream().parallel().filter(islandUpgrade -> islandUpgrade.getType() == type).toList();
    }

}
