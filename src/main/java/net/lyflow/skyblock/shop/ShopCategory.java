package net.lyflow.skyblock.shop;

import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ShopCategory {

    BLOCK("Block", new ItemBuilder(Material.COBBLESTONE).setName("Block"), 2),
    ITEM("Item", new ItemBuilder(Material.LIGHT_BLUE_CANDLE).setName("Item"), 4),
    ORE("Ore", new ItemBuilder(Material.EMERALD).setName("Ore"), 6);

    private final String name;
    private final ItemBuilder itemBuilder;
    private final int pos;

    ShopCategory(String name, ItemBuilder itemBuilder, int pos) {
        this.name = name;
        this.itemBuilder = itemBuilder;
        this.pos = pos;
    }

    public ItemStack getItemStack() {
        return itemBuilder.toItemStack();
    }

    public int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public static ShopCategory getShopCategory(int slot) {
        return Arrays.stream(values()).filter(shopCategory -> shopCategory.getPos() == slot).findFirst().get();
    }

    public static ShopCategory getShopCategoryByInventoryName(String inventoryName) {
        return Arrays.stream(values()).filter(shopCategory -> inventoryName.contains(shopCategory.getName())).findFirst().get();
    }
}
