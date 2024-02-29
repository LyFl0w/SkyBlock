package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;
import net.lyflow.skyblock.island.upgrade.mod.TntDropRateUpgrade;
import net.lyflow.skyblock.utils.iteminfo.ItemInfo;
import net.lyflow.skyblock.utils.iteminfo.UniqueItemInfo;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

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
        addIslandUpgrades(
                new CobblestoneGeneratorUpgrade(skyblock, 0,
                        new CobblestoneGeneratorUpgrade.CobblestoneGeneratorLevelUpgrade(
                                List.of(1_000f, 2_000f, 3_000f),
                                new LinkedHashSet<>(List.of(1, 2, 3)),
                                List.of(CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1.getGenerator(),
                                        CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1_2.getGenerator(),
                                        CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1_3.getGenerator()
                                ),
                                List.of(List.of("Passer au niveau suppérieur"),
                                        List.of("Toujours plus ?!"),
                                        List.of("Gourmand !")
                                )
                        ),
                        ItemInfo.of(1, Material.COAL_ORE, "Cobblestone Upgrade n°1",
                                "Vous en avez marre de casser du bois, d'acheter de la redstone, ou du lapis ?",
                                "Avec cette amélioration et un block de charbon en dessous de votre lave")
                ),

                new CobblestoneGeneratorUpgrade(skyblock, 1, CobblestoneGeneratorUpgrade.Generator
                        .PreGenerator.UPGRADE_2.getGenerator(), 1_000,
                        ItemInfo.of(2, Material.IRON_ORE, "Cobblestone Upgrade n°2",
                                "Vous en avez marre d'acheter tout vos minerais ?'",
                                "Avec cette amélioration et un block de fer en dessous de votre lave",
                                "Vous pourrez obtenir les minerais suivants :")
                ),

                new CobblestoneGeneratorUpgrade(skyblock, 2, CobblestoneGeneratorUpgrade.Generator.
                        PreGenerator.UPGRADE_3.getGenerator(), 1_000,
                        ItemInfo.of(3, Material.NETHER_QUARTZ_ORE, "Cobblestone Upgrade n°3",
                                "Vous en avez marre d'acheter du quartz ?",
                                "Avec cette amélioration et un block de netherrack en dessous de votre lave",
                                "Vous pourrez obtenir les minerais suivants :")
                ),

                new TntDropRateUpgrade(skyblock, 4, 0.5f,
                        new TntDropRateUpgrade.TntDropRateLevelUpgrade(
                                List.of(1_000f, 2_000f, 3_000f),
                                new LinkedHashSet<>(List.of(1, 2, 3)),
                                List.of(0.6f, 0.8f, 1.0f),
                                List.of(List.of("Vous en avez marre des 50% ?", "En voilà 60% !"),
                                        List.of("Vous en avez marre des 60% ?", "En voilà 80% !"),
                                        List.of("Coquin ! 80% est inssufisant ?", "EN VOILÀ 100% !")
                                )
                        ),
                        UniqueItemInfo.of(4, Material.TNT, "Tnt Drop Rate Upgrade")
                )
        );
    }

    private void registerUpgradeEvent(SkyBlock skyblock, PluginManager pluginManager) {
        pluginManager.registerEvents(new CobblestoneGeneratorUpgrade.ListenerEvent(this), skyblock);
        pluginManager.registerEvents(new TntDropRateUpgrade.ListenerEvent(this), skyblock);
    }

    private void addIslandUpgrades(IslandUpgrade... islandUpgrades) {
        this.islandUpgrades.addAll(Arrays.stream(islandUpgrades).toList());
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
