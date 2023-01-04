package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.upgrade.IslandUpgrade;
import net.lyflow.skyblock.upgrade.mod.CobblestoneGeneratorUpgrade;

import org.bukkit.Material;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandUpgradeManager {

    private final List<IslandUpgrade> islandUpgrades = new ArrayList<>();

    public IslandUpgradeManager(SkyBlock skyblock) {
        registerIslandUpgrades(skyblock);
    }

    private void registerIslandUpgrades(SkyBlock skyblock) {
        addIslandUpgrades(
                new CobblestoneGeneratorUpgrade(skyblock, 0, true, 1_000, 1, Material.COAL_ORE,  "Cobblestone Upgrade n°1", "Vous en avez marre de casser du bois, d'acheter de la redstone, ou du lapis ?", "Avec cette amélioration et un block de charbon en dessous de votre lave", "Et vous pourrez obtenir uniquement de manière aléatoire les minerais suivants :", "- cobblestone", "- charbon", "- redstone", "- lapis"),
                new CobblestoneGeneratorUpgrade(skyblock, 1, true, 1_000, 2, Material.NETHER_QUARTZ_ORE,  "Cobblestone Upgrade n°2", "Vous en avez marre d'acheter du quartz ?", "Avec cette amélioration et un block de netherrack en dessous de votre lave", "Et vous pourrez obtenir uniquement de manière aléatoire les minerais suivants :", "- netherrack", "- quartz", "- gold")
        );
    }

    private void addIslandUpgrades(IslandUpgrade... islandUpgrades) {
        this.islandUpgrades.addAll(Arrays.stream(islandUpgrades).toList());
    }

    public List<IslandUpgrade> getIslandUpgrades() {
        return islandUpgrades;
    }

    public List<IslandUpgrade> getIslandUpgradesByType(IslandUpgrade.Type type) {
        return islandUpgrades.stream().parallel().filter(islandUpgrade -> islandUpgrade.getType() == type).toList();
    }

    public List<IslandUpgrade> getIslandUpgradesBySave(boolean save) {
        return islandUpgrades.stream().parallel().filter(islandUpgrade -> islandUpgrade.isSave() == save).toList();
    }

}
