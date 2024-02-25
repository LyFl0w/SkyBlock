package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.mod.entity.ReproduceAnimalChallenge;
import net.lyflow.skyblock.upgrade.IslandUpgrade;
import net.lyflow.skyblock.upgrade.mod.CobblestoneGeneratorUpgrade;

import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IslandUpgradeManager {

    private final List<IslandUpgrade> islandUpgrades = new ArrayList<>();

    public IslandUpgradeManager(SkyBlock skyblock) {
        registerIslandUpgrades(skyblock);
        registerUpgradeEvent(skyblock, skyblock.getServer().getPluginManager());
    }

    private void registerIslandUpgrades(SkyBlock skyblock) {
        addIslandUpgrades(
                new CobblestoneGeneratorUpgrade(skyblock, CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_1,
                        1_000, 1, Material.COAL_ORE, Material.COAL_BLOCK, "Cobblestone Upgrade n°1",
                        "Vous en avez marre de casser du bois, d'acheter de la redstone, ou du lapis ?",
                        "Avec cette amélioration et un block de charbon en dessous de votre lave",
                        "Vous pourrez obtenir les minerais suivants :"),
                
                new CobblestoneGeneratorUpgrade(skyblock, CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_2,
                        1_000, 2, Material.IRON_ORE, Material.IRON_BLOCK, "Cobblestone Upgrade n°2",
                        "Vous en avez marre d'acheter tout vos minerais ?'",
                        "Avec cette amélioration et un block de fer en dessous de votre lave",
                        "Vous pourrez obtenir les minerais suivants :"),
                
                new CobblestoneGeneratorUpgrade(skyblock, CobblestoneGeneratorUpgrade.Generator.PreGenerator.UPGRADE_3,
                        1_000, 3, Material.NETHER_QUARTZ_ORE, Material.QUARTZ_BLOCK,  "Cobblestone Upgrade n°3",
                        "Vous en avez marre d'acheter du quartz ?",
                        "Avec cette amélioration et un block de netherrack en dessous de votre lave",
                        "Vous pourrez obtenir les minerais suivants :")
        );
    }

    private void registerUpgradeEvent(SkyBlock skyblock, PluginManager pluginManager) {
        pluginManager.registerEvents(new CobblestoneGeneratorUpgrade.ListenerEvent(this), skyblock);
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
            if(islandUpgrade.getID() == id) return islandUpgrade;
        }
        return null;
    }

    @Nullable
    public IslandUpgrade getIslandUpgradesBySlot(int slot) {
        for (final IslandUpgrade islandUpgrade : islandUpgrades) {
            if(islandUpgrade.getSlot() == slot) return islandUpgrade;
        }
        return null;
    }

    public List<IslandUpgrade> getIslandUpgradesByType(IslandUpgrade.Type type) {
        return islandUpgrades.stream().parallel().filter(islandUpgrade -> islandUpgrade.getType() == type).toList();
    }

}
