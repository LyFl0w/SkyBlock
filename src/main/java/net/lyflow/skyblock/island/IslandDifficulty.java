package net.lyflow.skyblock.island;

import org.bukkit.Material;

import java.util.Arrays;

public enum IslandDifficulty {

    EASY(1, "§aFacile", Material.LIME_STAINED_GLASS_PANE),
    NORMAL(2, "§eNormal", Material.YELLOW_STAINED_GLASS_PANE),
    HARD(3, "§cDifficile", Material.RED_STAINED_GLASS_PANE);

    private final int difficulty;
    private final Material material;
    private final String itemName;

    IslandDifficulty(int difficulty, String itemName, Material material) {
        this.difficulty = difficulty;
        this.material = material;
        this.itemName = itemName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return itemName;
    }

    public static IslandDifficulty getIslandDifficultyByMaterial(Material material) {
        return Arrays.stream(values()).filter(islandDifficulty -> islandDifficulty.getMaterial() == material).findFirst().get();
    }
}