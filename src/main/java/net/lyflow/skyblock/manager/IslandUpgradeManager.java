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
import net.lyflow.skyblock.loader.island.upgrade.mod.CobblestoneGeneratorData;
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

        final IslandUpgradeData upgrade4 = new IslandUpgradeData(4, 4, "tnt_drop_rate",
                "minecraft:tnt", "Tnt Drop Rate Upgrade", List.of(),
                List.of(
                        new LevelUpgrade(1_000, 1, List.of("Vous en avez marre des 50% ?", "En voilà 60% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.6)),
                        new LevelUpgrade(2_000, 2, List.of("Vous en avez marre des 60% ?", "En voilà 80% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.8)),
                        new LevelUpgrade(3_000, 3, List.of("Coquin !!!", "En voilà 100% !"), Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 1.0))
                ),
                Map.of(LevelUpgradeKey.DROP_RATE.getKey(), 0.5)
        );

        /*

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
                        IslandUpgrade.ItemInfo.of(1, Material.COAL_ORE, "Cobblestone Upgrade n°1",
                                "Vous en avez marre de casser du bois, d'acheter de la redstone, ou du lapis ?",
                                "Avec cette amélioration et un block de charbon en dessous de votre lave")
                ),

                new CobblestoneGeneratorUpgrade(skyblock, 1, CobblestoneGeneratorUpgrade.Generator
                        .PreGenerator.UPGRADE_2.getGenerator(), 1_000,
                        IslandUpgrade.ItemInfo.of(2, Material.IRON_ORE, "Cobblestone Upgrade n°2",
                                "Vous en avez marre d'acheter tout vos minerais ?'",
                                "Avec cette amélioration et un block de fer en dessous de votre lave",
                                "Vous pourrez obtenir les minerais suivants :")
                ),

                new CobblestoneGeneratorUpgrade(skyblock, 2, CobblestoneGeneratorUpgrade.Generator.
                        PreGenerator.UPGRADE_3.getGenerator(), 1_000,
                        IslandUpgrade.ItemInfo.of(3, Material.NETHER_QUARTZ_ORE, "Cobblestone Upgrade n°3",
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
                        IslandUpgrade.UniqueItemInfo.of(4, Material.TNT, "Tnt Drop Rate Upgrade")
                )




        final IslandUpgradeData upgrade = new IslandUpgradeData(0, 1, "cobblestone_generator",
                "minecraft:coal_ore",  "Cobblestone Upgrade n°1",
                List.of(
                        "Vous en avez marre de casser du bois, d'acheter de la redstone, ou du lapis ?",
                        "Avec cette amélioration et un block de charbon en dessous de votre lave"
                ),
                List.of(
                        new LevelUpgrade(1_000, 1, List.of("Passer au niveau suppérieur"),
                                Map.of(LevelUpgradeKey.GENERATOR.getKey(), CobblestoneGeneratorUpgrade.Generator
                                        .PreGenerator.UPGRADE_1.getGenerator())),
                        new LevelUpgrade(2_000, 2, List.of("Toujours plus ?!"),
                                Map.of(LevelUpgradeKey.GENERATOR.getKey(), CobblestoneGeneratorUpgrade.Generator
                                        .PreGenerator.UPGRADE_1_2.getGenerator())),
                        new LevelUpgrade(3_000, 3, List.of("Gourmand !"),
                                Map.of(LevelUpgradeKey.GENERATOR.getKey(), CobblestoneGeneratorUpgrade.Generator
                                        .PreGenerator.UPGRADE_1_3.getGenerator()))
                )
        );*/

        final IslandUpgradeData upgrade = new IslandUpgradeData(0, 1, "cobblestone_generator",
                "minecraft:coal_ore",  "Cobblestone Upgrade n°1",
                List.of(
                        "Vous en avez marre de casser du bois, d'acheter de la redstone, ou du lapis ?",
                        "Avec cette amélioration et un block de charbon en dessous de votre lave"
                ),
                List.of(
                        new LevelUpgrade(1_000, 1, List.of("Passer au niveau suppérieur"),
                                Map.of(LevelUpgradeKey.GENERATOR.getKey(),
                                        new CobblestoneGeneratorData(
                                                "minecraft:cobblestone",
                                                "minecraft:coal_block",
                                                List.of(
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:coal_ore", 0.2),
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:lapis_ore", 0.2),
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:redstone_ore", 0.3)
                                                )
                                        ))),
                        new LevelUpgrade(2_000, 2, List.of("Toujours plus ?!"),
                                Map.of(LevelUpgradeKey.GENERATOR.getKey(),
                                        new CobblestoneGeneratorData(
                                                "minecraft:cobblestone",
                                                "minecraft:coal_block",
                                                List.of(
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:coal_ore", 0.25),
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:lapis_ore", 0.25),
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:redstone_ore", 0.32)
                                                )
                                        ))),
                        new LevelUpgrade(3_000, 3, List.of("Gourmand !"),
                                Map.of(LevelUpgradeKey.GENERATOR.getKey(),
                                        new CobblestoneGeneratorData(
                                                "minecraft:cobblestone",
                                                "minecraft:coal_block",
                                                List.of(
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:coal_ore", 0.3),
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:lapis_ore", 0.3),
                                                        new CobblestoneGeneratorData.MaterialEntry("minecraft:redstone_ore", 0.4)
                                                )
                                        )))
                )
        );

        /*final IslandUpgradeData upgrade2 = new IslandUpgradeData(1, 2, "cobblestone_generator",
                "minecraft:iron_ore",  "Cobblestone Upgrade n°2",
                List.of(
                        "Vous en avez marre d'acheter tout vos minerais ?",
                        "Avec cette amélioration et un block de fer en dessous de votre lave",
                        "Vous pourrez obtenir les minerais suivants :"
                ),
                List.of(new LevelUpgrade(1_000, 2, List.of(), Map.of(LevelUpgradeKey.GENERATOR.getKey(), CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_2.getGenerator())))
        );

        final IslandUpgradeData upgrade3 = new IslandUpgradeData(2, 3, "cobblestone_generator",
                "minecraft:nether_quartz_ore",  "Cobblestone Upgrade n°3",
                List.of(
                        "Vous en avez marre d'acheter du quartz ?",
                        "Avec cette amélioration et un block de netherrack en dessous de votre lave",
                        "Vous pourrez obtenir les minerais suivants :"
                ),
                List.of(new LevelUpgrade(1_000, 3, List.of(), Map.of(LevelUpgradeKey.GENERATOR.getKey(), CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_3.getGenerator())))
        );*/


        System.out.println(gson.toJson(upgrade));
        /*System.out.println(gson.toJson(upgrade2));
        System.out.println(gson.toJson(upgrade3));*/

        try {
            addIslandUpgrade(upgrade.toUpgrade(skyblock));
            /*addIslandUpgrade(upgrade2.toUpgrade(skyblock));
            addIslandUpgrade(upgrade3.toUpgrade(skyblock));*/
            addIslandUpgrade(upgrade4.toUpgrade(skyblock));
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
