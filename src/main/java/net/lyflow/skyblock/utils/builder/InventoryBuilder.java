package net.lyflow.skyblock.utils.builder;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryBuilder implements Cloneable {

    private Inventory inventory;

    public InventoryBuilder(InventoryType inventoryType, String title) {
        inventory = Bukkit.createInventory(null, inventoryType, title);
    }

    public InventoryBuilder(InventoryType inventoryType) {
        this(inventoryType, "");
    }

    public InventoryBuilder(int size, String title) {
        inventory = Bukkit.createInventory(null, size, title);
    }

    public InventoryBuilder(int size) {
        this(size, null);
    }

    public InventoryBuilder(Inventory inventory) {
        this.inventory = inventory;
    }

    public InventoryBuilder(Inventory inventory, String title) {
        this.inventory = Bukkit.createInventory(null, inventory.getSize(), title);
        this.inventory.setContents(inventory.getContents());
    }

    public InventoryBuilder addItem(ItemStack item) {
        inventory.addItem(item);
        return this;
    }

    public InventoryBuilder setItem(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    public InventoryBuilder setTitle(String title) {
        inventory = new InventoryBuilder(inventory, title).toInventory();
        return this;
    }

    public boolean isEmptySlot(int slot) {
        final ItemStack itemStack = inventory.getItem(slot);
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public InventoryBuilder setContents(ItemStack[] items) {
        inventory.setContents(items);
        return this;
    }

    public InventoryBuilder clear() {
        inventory.clear();
        return this;
    }

    public Inventory toInventory() {
        return inventory;
    }

    @Override
    public InventoryBuilder clone() {
        try {
            // copy mutable state here, so the clone can't change the internals of the original
            return (InventoryBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Error Inventory Clone");
        }
    }
}
