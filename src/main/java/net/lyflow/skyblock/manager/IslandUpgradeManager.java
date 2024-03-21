package net.lyflow.skyblock.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;
import net.lyflow.skyblock.island.upgrade.mod.TntDropRateUpgrade;
import net.lyflow.skyblock.loader.gson.EmptyListToNullFactory;
import net.lyflow.skyblock.loader.island.upgrade.IslandUpgradeData;
import net.lyflow.skyblock.utils.ResourceUtils;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        final String name = "data/upgrade";
        final File upgradeFolder = new File(skyblock.getDataFolder(), name);
        if(!upgradeFolder.exists()) {
            skyblock.getLogger().info("Generate Upgrade folder in plugin folder (" + name + ")");
            ResourceUtils.saveResourceFolder(name, upgradeFolder, skyblock, false);
        }

        for(File configurationFile : Objects.requireNonNull(upgradeFolder.listFiles())) {
            try {
                final IslandUpgradeData islandUpgradeData = gson.fromJson(new FileReader(configurationFile), IslandUpgradeData.class);
                addIslandUpgrade(islandUpgradeData.toUpgrade(skyblock));
            } catch (FileNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
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
