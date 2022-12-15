package net.lyflow.skyblock.island;

import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public enum IslandDifficulty {

    EASY(1, "Facile", Material.LIME_STAINED_GLASS_PANE), NORMAL(2, "Normal", Material.YELLOW_STAINED_GLASS_PANE), HARD(3, "Difficile", Material.RED_STAINED_GLASS_PANE);

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

    public String getItemName() {
        return itemName;
    }

    public static IslandDifficulty getIslandDifficultyByMaterial(Material material) {
        return Arrays.stream(values()).filter(islandDifficulty -> islandDifficulty.getMaterial() == material).findFirst().get();
    }

    public static Inventory openInventoryDifficulty() {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Difficulté de l'île");

        final AtomicInteger slot = new AtomicInteger(1);
        Arrays.stream(values()).forEach(islandDifficulty ->
                inventoryBuilder.setItem(slot.getAndAdd(2), new ItemBuilder(islandDifficulty.getMaterial()).setName("§r"+islandDifficulty.getItemName()).toItemStack()));
        inventoryBuilder.setItem(slot.get(), new ItemBuilder(Material.STRUCTURE_VOID).setName("§rAttendre une invitation").toItemStack());

        return inventoryBuilder.toInventory();
    }
}