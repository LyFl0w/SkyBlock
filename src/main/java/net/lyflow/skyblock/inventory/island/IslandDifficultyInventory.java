package net.lyflow.skyblock.inventory.island;

import net.lyflow.skyblock.island.IslandDifficulty;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class IslandDifficultyInventory {

    public static Inventory getInventory() {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Difficulté de l'île");

        final AtomicInteger slot = new AtomicInteger(1);
        Arrays.stream(IslandDifficulty.values()).forEach(islandDifficulty ->
                inventoryBuilder.setItem(slot.getAndAdd(2), new ItemBuilder(islandDifficulty.getMaterial()).setName("§r" + islandDifficulty.getName()).toItemStack()));
        inventoryBuilder.setItem(slot.get(), new ItemBuilder(Material.STRUCTURE_VOID).setName("§rAttendre une invitation").toItemStack());

        return inventoryBuilder.toInventory();
    }

}
