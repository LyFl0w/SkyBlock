package net.lyflow.skyblock.shop;

import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ItemShop {
    // BLOCK
    BLOCK_OAK_LOG(Material.OAK_LOG, 3, 2, ShopCategory.BLOCK),
    BLOCK_SPRUCE_LOG(Material.SPRUCE_LOG, 3, 2, ShopCategory.BLOCK),
    BLOCK_BIRCH_LOG(Material.BIRCH_LOG, 3, 2, ShopCategory.BLOCK),
    BLOCK_JUNGLE_LOG(Material.JUNGLE_LOG, 3, 2, ShopCategory.BLOCK),
    BLOCK_ACACIA_LOG(Material.ACACIA_LOG, 3, 2, ShopCategory.BLOCK),
    BLOCK_DARK_OAK_LOG(Material.DARK_OAK_LOG, 3, 2, ShopCategory.BLOCK),
    BLOCK_MANGROVE_LOG(Material.MANGROVE_LOG, 3, 2, ShopCategory.BLOCK),

    COBBLESTONE(Material.COBBLESTONE, 2, 1, ShopCategory.BLOCK),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, 3, 2, ShopCategory.BLOCK),
    BLACK_STONE(Material.BLACKSTONE, 2, 1, ShopCategory.BLOCK),

    SAND(Material.SAND, 4, 3, ShopCategory.BLOCK),
    RED_SAND(Material.RED_SAND, 4, 3, ShopCategory.BLOCK),

    BUDDING_AMETHYST(Material.BUDDING_AMETHYST, 10, -1, ShopCategory.BLOCK),

    // ITEM

    BLUE_CANDLE(Material.BLUE_CANDLE, -1, 10, ShopCategory.ITEM);


    
    private final double buyPrice, sellPrice;
    private final Material material;
    private final ShopCategory shopCategory;

    ItemShop(Material material, double buyPrice, double sellPrice, ShopCategory shopCategory) {
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.material = material;
        this.shopCategory = shopCategory;
    }

    public static ItemShop getItemShopByMaterial(Material material) {
        return Arrays.stream(values()).parallel().filter(itemShop -> itemShop.getMaterial() == material).findFirst().get();
    }

    public ItemBuilder getItemBuilder(int number) {
        return new ItemBuilder(material, number);
    }

    public ItemStack getBuyItemStack(int number) {
        return getItemBuilder(number).setLore("§a"+buyPrice*number+"$").toItemStack();
    }

    public ItemStack getSellItemStack(int number) {
        return getItemBuilder(number).setLore("§a"+sellPrice*number+"$").toItemStack();
    }

    public Material getMaterial() {
        return material;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public boolean isPurchasable() {
        return buyPrice != -1;
    }

    public boolean isSaleable() {
        return sellPrice != -1;
    }
}
