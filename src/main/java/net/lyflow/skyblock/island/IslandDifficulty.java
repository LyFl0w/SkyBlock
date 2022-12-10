package net.lyflow.skyblock.island;

import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public enum IslandDifficulty {

    EASY(1, Material.LIME_STAINED_GLASS_PANE), NORMAL(2, Material.YELLOW_STAINED_GLASS_PANE), HARD(3, Material.RED_STAINED_GLASS_PANE);

    private final int difficulty;
    private final Material material;

    IslandDifficulty(int difficulty, Material material) {
        this.difficulty = difficulty;
        this.material = material;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public Material getMaterial() {
        return material;
    }

    public static IslandDifficulty getIslandDifficultyByMaterial(Material material) {
        return Arrays.stream(values()).filter(islandDifficulty -> islandDifficulty.getMaterial() == material).findFirst().get();
    }

    public static Inventory openInventoryDifficulty() {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Island Difficulty");
        final AtomicInteger slot = new AtomicInteger(0);
        Arrays.stream(values()).forEach(islandDifficulty ->
                inventoryBuilder.setItem(slot.addAndGet(2), new ItemBuilder(islandDifficulty.getMaterial()).setName("§r"+islandDifficulty.name()).toItemStack()));
        return inventoryBuilder.toInventory();
    }
}