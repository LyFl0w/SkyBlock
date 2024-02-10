package net.lyflow.skyblock.shop;

import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;

public enum ShopCategory {

    BLOCK("Block", new ItemBuilder(Material.COBBLESTONE), 2),
    ITEM("Item", new ItemBuilder(Material.LIGHT_BLUE_CANDLE), 4),
    SPAWN_EGG("Spawn Eggs", new ItemBuilder(Material.VILLAGER_SPAWN_EGG), 6),
    ORE("Ore", new ItemBuilder(Material.EMERALD), 8);

    private final String name;
    private final ItemBuilder itemBuilder;
    private final int pos;

    ShopCategory(String name, ItemBuilder itemBuilder, int pos) {
        this.name = name;
        this.itemBuilder = itemBuilder;
        this.pos = pos;
    }

    public static ShopCategory getShopCategory(int slot) {
        final Optional<ShopCategory> optionalShopCategory = Arrays.stream(values()).filter(shopCategory -> shopCategory.getPos() == slot).findFirst();
        if (optionalShopCategory.isEmpty())
            throw new IllegalArgumentException("Le slot " + slot + " ne fait référence à aucune catégorie du shop");
        return optionalShopCategory.get();
    }

    public static ShopCategory getShopCategoryByInventoryName(String inventoryName) {
        final Optional<ShopCategory> optionalShopCategory = Arrays.stream(values()).filter(shopCategory -> inventoryName.contains(shopCategory.getName())).findFirst();
        if (optionalShopCategory.isEmpty())
            throw new IllegalArgumentException("Le nom d'inventaire " + inventoryName + " ne fait référence à aucune catégorie du shop");
        return optionalShopCategory.get();
    }

    public ItemStack getItemStack() {
        return itemBuilder.setName(name).toItemStack();
    }

    public int getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }
}
