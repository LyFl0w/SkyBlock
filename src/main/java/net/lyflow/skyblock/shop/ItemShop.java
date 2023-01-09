package net.lyflow.skyblock.shop;

import net.lyflow.skyblock.utils.builder.ItemBuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ItemShop {

    // BLOCK
    DIRT(Material.DIRT, 10, 3, ShopCategory.BLOCK),

    OAK_LOG(Material.OAK_LOG, 5, 2, ShopCategory.BLOCK),
    SPRUCE_LOG(Material.SPRUCE_LOG, 5, 2, ShopCategory.BLOCK),
    BIRCH_LOG(Material.BIRCH_LOG, 5, 2, ShopCategory.BLOCK),
    JUNGLE_LOG(Material.JUNGLE_LOG, 5, 2, ShopCategory.BLOCK),
    ACACIA_LOG(Material.ACACIA_LOG, 5, 2, ShopCategory.BLOCK),
    DARK_OAK_LOG(Material.DARK_OAK_LOG, 5, 2, ShopCategory.BLOCK),
    MANGROVE_LOG(Material.MANGROVE_LOG, 5, 2, ShopCategory.BLOCK),

    COBBLESTONE(Material.COBBLESTONE, 2, 1, ShopCategory.BLOCK),
    MOSSY_COBBLESTONE(Material.MOSSY_COBBLESTONE, 3, 2, ShopCategory.BLOCK),
    BLACK_STONE(Material.BLACKSTONE, 2, 1, ShopCategory.BLOCK),
    POINTED_DRIPSTONE(Material.POINTED_DRIPSTONE, 300, 50, ShopCategory.BLOCK),

    SAND(Material.SAND, 4, 2, ShopCategory.BLOCK),
    RED_SAND(Material.RED_SAND, 4, 2, ShopCategory.BLOCK),
    SOUL_SAND(Material.SOUL_SAND, 25, 5, ShopCategory.BLOCK),
    GRAVEL(Material.GRAVEL, 4, 2, ShopCategory.BLOCK),
    CLAY(Material.CLAY, 5, 2, ShopCategory.BLOCK),
    ICE(Material.ICE, 10, 1, ShopCategory.BLOCK),
    PACKED_ICE(Material.PACKED_ICE, 50, 5, ShopCategory.BLOCK),
    BLUE_ICE(Material.BLUE_ICE, 100, 10, ShopCategory.BLOCK),

    END_STONE(Material.END_STONE, 50, 25, ShopCategory.BLOCK),
    GLOWSTONE(Material.GLOWSTONE, 10, 1, ShopCategory.BLOCK),
    OCHRE_FROGLIGHT(Material.OCHRE_FROGLIGHT, 50, 5, ShopCategory.BLOCK),
    VERDANT_FROGLIGHT(Material.VERDANT_FROGLIGHT, 50, 5, ShopCategory.BLOCK),
    PEARLESCENT_FROGLIGHT(Material.PEARLESCENT_FROGLIGHT, 50, 5, ShopCategory.BLOCK),
    RED_MUSHROOM_BLOCK(Material.RED_MUSHROOM_BLOCK, 10, 1, ShopCategory.BLOCK),
    BROWN_MUSHROOM_BLOCK(Material.BROWN_MUSHROOM_BLOCK, 10, 1, ShopCategory.BLOCK),

    BUDDING_AMETHYST(Material.BUDDING_AMETHYST, 30, -1, ShopCategory.BLOCK),

    COBWEB(Material.COBWEB, 20, -1, ShopCategory.BLOCK),
    SCULK_SENSOR(Material.SCULK_SENSOR, 50, -1, ShopCategory.BLOCK),
    DEAD_HORN_CORAL_FAN(Material.DEAD_HORN_CORAL_FAN,  10_000, 1_000, ShopCategory.BLOCK),


    // ITEM

    OAK_SAPLING(Material.OAK_SAPLING, 15, 0.25f, ShopCategory.ITEM),
    SPRUCE_SAPLING(Material.SPRUCE_SAPLING, 15, 0.25f, ShopCategory.ITEM),
    BIRCH_SAPLING(Material.BIRCH_SAPLING, 15, 0.25f, ShopCategory.ITEM),
    JUNGLE_SAPLING(Material.JUNGLE_SAPLING, 15, 0.25f, ShopCategory.ITEM),
    ACACIA_SAPLING(Material.ACACIA_SAPLING, 15, 0.25f, ShopCategory.ITEM),
    DARK_OAK_SAPLING(Material.DARK_OAK_SAPLING, 15, 0.25f, ShopCategory.ITEM),
    MANGROVE_PROPAGULE(Material.MANGROVE_PROPAGULE, 15, 0.25f, ShopCategory.ITEM),

    VINE(Material.VINE, 2, 0.1f, ShopCategory.ITEM),
    WEEPING_VINES(Material.WEEPING_VINES, 2, 0.1f, ShopCategory.ITEM),
    TWISTING_VINES(Material.TWISTING_VINES, 2, 0.1f, ShopCategory.ITEM),

    APPLE(Material.APPLE, -1, 5, ShopCategory.ITEM),
    BAMBOO(Material.BAMBOO, 30, 0.5f, ShopCategory.ITEM),
    SUGAR_CANE(Material.SUGAR_CANE, 30, 5, ShopCategory.ITEM),
    CACTUS(Material.CACTUS, 50, 3.5f, ShopCategory.ITEM),
    CHORUS_FLOWER(Material.CHORUS_FLOWER, 100, -1, ShopCategory.ITEM),
    CHORUS_FRUIT(Material.CHORUS_FRUIT, -1, 1, ShopCategory.ITEM),

    WATER_BUCKET(Material.WATER_BUCKET, 80, -1, ShopCategory.ITEM),
    POWDER_SNOW_BUCKET(Material.POWDER_SNOW_BUCKET, 85, -1, ShopCategory.ITEM),
    LAVA_BUCKET(Material.LAVA_BUCKET, 100, 55, ShopCategory.ITEM),

    ROTTEN_FLESH(Material.ROTTEN_FLESH, -1, 2, ShopCategory.ITEM),
    BONE(Material.BONE, 10, 2, ShopCategory.ITEM),
    STRING(Material.STRING, 5, 2, ShopCategory.ITEM),
    SNOWBALL(Material.SNOWBALL, 2, 0.25f, ShopCategory.ITEM),
    EGG(Material.EGG, 15, 2, ShopCategory.ITEM),
    FEATHER(Material.FEATHER, 10, 1.5f, ShopCategory.ITEM),
    LEATHER(Material.LEATHER, 15, 2, ShopCategory.ITEM),
    RABBIT_HIDE(Material.RABBIT_HIDE, 15, 2, ShopCategory.ITEM),
    HONEYCOMB(Material.HONEYCOMB, 30, 3, ShopCategory.ITEM),
    INK_SAC(Material.INK_SAC, 2, 1, ShopCategory.ITEM),
    GLOW_INK_SAC(Material.GLOW_INK_SAC, 10, 7, ShopCategory.ITEM),
    SCUTE(Material.SCUTE, 10, 7, ShopCategory.ITEM),
    SLIME_BALL(Material.SLIME_BALL, 20, 5, ShopCategory.ITEM),
    ENDER_PEARL(Material.ENDER_PEARL, 5, 2, ShopCategory.ITEM),
    GUNPOWDER(Material.GUNPOWDER, 15, 4, ShopCategory.ITEM),
    HONEY_BOTTLE(Material.HONEY_BOTTLE, 20, 5, ShopCategory.ITEM),

    WHEAT_SEEDS(Material.WHEAT_SEEDS, 3, 0.5f, ShopCategory.ITEM),
    WHEAT(Material.WHEAT, 2, 2, ShopCategory.ITEM),
    CARROT(Material.CARROT, 3, 2, ShopCategory.ITEM),
    POTATO(Material.POTATO, 3, 2, ShopCategory.ITEM),
    COCOA_BEANS(Material.COCOA_BEANS, 3, 1, ShopCategory.ITEM),
    BEETROOT_SEEDS(Material.BEETROOT_SEEDS, 3, 0.5f, ShopCategory.ITEM),
    BEETROOT(Material.BEETROOT, -1, 1, ShopCategory.ITEM),
    PUMPKIN_SEEDS(Material.PUMPKIN_SEEDS, 10, 0.5f, ShopCategory.ITEM),
    MELON_SEEDS(Material.MELON_SEEDS, 10, 0.5f, ShopCategory.ITEM),
    BROWN_MUSHROOM(Material.BROWN_MUSHROOM, 2, 0.5f, ShopCategory.ITEM),
    RED_MUSHROOM(Material.RED_MUSHROOM, 2, 0.5f, ShopCategory.ITEM),
    GLOW_BERRIES(Material.GLOW_BERRIES, 5, 0.5f, ShopCategory.ITEM),
    SWEET_BERRIES(Material.SWEET_BERRIES, 5, 0.5f, ShopCategory.ITEM),
    NETHER_WART(Material.NETHER_WART, 12, 2, ShopCategory.ITEM),

    COD(Material.COD, -1, 5, ShopCategory.ITEM),
    SALMON(Material.SALMON, -1, 5, ShopCategory.ITEM),
    TROPICAL_FISH(Material.TROPICAL_FISH, -1, 5, ShopCategory.ITEM),
    PUFFERFISH(Material.PUFFERFISH, -1, 5, ShopCategory.ITEM),


    PRISMARINE_SHARD(Material.PRISMARINE_SHARD, 15, 3, ShopCategory.ITEM),
    PRISMARINE_CRYSTALS(Material.PRISMARINE_CRYSTALS, 15, 3, ShopCategory.ITEM),
    NAUTILUS_SHELL(Material.NAUTILUS_SHELL, 30, 5, ShopCategory.ITEM),
    HEART_OF_THE_SEA(Material.HEART_OF_THE_SEA, 500, 10, ShopCategory.ITEM),
    CONDUIT(Material.CONDUIT, 250, 5, ShopCategory.ITEM),
    BLAZE_ROD(Material.BLAZE_ROD, 10, 8, ShopCategory.ITEM),
    NETHER_STAR(Material.NETHER_STAR, 350_000, 150, ShopCategory.ITEM),
    ELYTRA(Material.ELYTRA, 500_000, 300, ShopCategory.ITEM),
    SHULKER_SHELL(Material.SHULKER_SHELL, 100_000, 50, ShopCategory.ITEM),

    CREEPER_HEAD(Material.CREEPER_HEAD, -1, 100, ShopCategory.ITEM),
    ZOMBIE_HEAD(Material.ZOMBIE_HEAD, -1, 150, ShopCategory.ITEM),
    PIGLIN_HEAD(Material.PIGLIN_HEAD, -1, 150, ShopCategory.ITEM),
    SKELETON_SKULL(Material.SKELETON_SKULL, -1, 120, ShopCategory.ITEM),
    WITHER_SKELETON_SKULL(Material.WITHER_SKELETON_SKULL, 100_000, 80, ShopCategory.ITEM),

    MUSIC_DISC_5(Material.MUSIC_DISC_5, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_11(Material.MUSIC_DISC_11, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_13(Material.MUSIC_DISC_13, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_CAT(Material.MUSIC_DISC_CAT, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_FAR(Material.MUSIC_DISC_FAR, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_MALL(Material.MUSIC_DISC_MALL, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_WARD(Material.MUSIC_DISC_WARD, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_CHIRP(Material.MUSIC_DISC_CHIRP, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_WAIT(Material.MUSIC_DISC_WAIT, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_STRAD(Material.MUSIC_DISC_STRAD, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_STAL(Material.MUSIC_DISC_STAL, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_PIGSTEP(Material.MUSIC_DISC_PIGSTEP, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_OTHERSIDE(Material.MUSIC_DISC_OTHERSIDE, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_MELLOHI(Material.MUSIC_DISC_MELLOHI, 100_000, 300, ShopCategory.ITEM),
    MUSIC_DISC_BLOCKS(Material.MUSIC_DISC_BLOCKS, 100_000, 300, ShopCategory.ITEM),

    DANDELION(Material.DANDELION, 2, 0.25f, ShopCategory.ITEM),
    POPPY(Material.POPPY, 2, 0.25f, ShopCategory.ITEM),
    BLUE_ORCHID(Material.BLUE_ORCHID, 2, 0.25f, ShopCategory.ITEM),
    ALLIUM(Material.ALLIUM, 2, 0.25f, ShopCategory.ITEM),
    AZURE_BLUET(Material.AZURE_BLUET, 2, 0.25f, ShopCategory.ITEM),
    RED_TULIP(Material.RED_TULIP, 2, 0.25f, ShopCategory.ITEM),
    ORANGE_TULIP(Material.ORANGE_TULIP, 2, 0.25f, ShopCategory.ITEM),
    WHITE_TULIP(Material.WHITE_TULIP, 2, 0.25f, ShopCategory.ITEM),
    PINK_TULIP(Material.PINK_TULIP, 2, 0.25f, ShopCategory.ITEM),
    OXEYE_DAISY(Material.OXEYE_DAISY, 2, 0.25f, ShopCategory.ITEM),
    CORNFLOWER(Material.CORNFLOWER, 2, 0.25f, ShopCategory.ITEM),
    LILY_OF_THE_VALLEY(Material.LILY_OF_THE_VALLEY, 2, 0.25f, ShopCategory.ITEM),
    SUNFLOWER(Material.SUNFLOWER, 2, 0.25f, ShopCategory.ITEM),
    LILAC(Material.LILAC, 2, 0.25f, ShopCategory.ITEM),
    ROSE_BUSH(Material.ROSE_BUSH, 2, 0.25f, ShopCategory.ITEM),
    SMALL_DRIPLEAF(Material.SMALL_DRIPLEAF, 2, 0.25f, ShopCategory.ITEM),
    BIG_DRIPLEAF(Material.BIG_DRIPLEAF, 2, 0.25f, ShopCategory.ITEM),
    SPORE_BLOSSOM(Material.SPORE_BLOSSOM, 2, 0.25f, ShopCategory.ITEM),

    // SPAWN EGG

    SPAWNER(Material.SPAWNER, 10_000_000, 10_000, ShopCategory.SPAWN_EGG),
    ALLAY_SPAWN_EGG(Material.ALLAY_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    AXOLOTL_SPAWN_EGG(Material.AXOLOTL_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    BEE_SPAWN_EGG(Material.BEE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    BLAZE_SPAWN_EGG(Material.BLAZE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    CAT_SPAWN_EGG(Material.CAT_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    CAVE_SPIDER_SPAWN_EGG(Material.CAVE_SPIDER_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    CHICKEN_SPAWN_EGG(Material.CHICKEN_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),

    COD_SPAWN_EGG(Material.COD_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    COW_SPAWN_EGG(Material.COW_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    CREEPER_SPAWN_EGG(Material.CREEPER_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    DOLPHIN_SPAWN_EGG(Material.DOLPHIN_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    DONKEY_SPAWN_EGG(Material.DONKEY_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    DROWNED_SPAWN_EGG(Material.DROWNED_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    ENDERMAN_SPAWN_EGG(Material.ENDERMAN_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    ENDERMITE_SPAWN_EGG(Material.ENDERMITE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),

    EVOKER_SPAWN_EGG(Material.EVOKER_SPAWN_EGG, 100_000_000, 10_000, ShopCategory.SPAWN_EGG),
    FOX_SPAWN_EGG(Material.FOX_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    FROG_SPAWN_EGG(Material.FROG_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    GHAST_SPAWN_EGG(Material.GHAST_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    GLOW_SQUID_SPAWN_EGG(Material.GLOW_SQUID_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    GOAT_SPAWN_EGG(Material.GOAT_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    GUARDIAN_SPAWN_EGG(Material.GUARDIAN_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    HORSE_SPAWN_EGG(Material.HORSE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),

    HUSK_SPAWN_EGG(Material.HUSK_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    IRON_GOLEM_SPAWN_EGG(Material.IRON_GOLEM_SPAWN_EGG, 100_000_000, 10_000, ShopCategory.SPAWN_EGG),
    LLAMA_SPAWN_EGG(Material.LLAMA_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    MAGMA_CUBE_SPAWN_EGG(Material.MAGMA_CUBE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    MULE_SPAWN_EGG(Material.MULE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    MOOSHROOM_SPAWN_EGG(Material.MOOSHROOM_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    OCELOT_SPAWN_EGG(Material.OCELOT_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    PANDA_SPAWN_EGG(Material.PANDA_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    PARROT_SPAWN_EGG(Material.PARROT_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),

    PIG_SPAWN_EGG(Material.PIG_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    PIGLIN_SPAWN_EGG(Material.PIGLIN_SPAWN_EGG, 15_000_000, 1_500, ShopCategory.SPAWN_EGG),
    PILLAGER_SPAWN_EGG(Material.PILLAGER_SPAWN_EGG, 15_000_000, 1_500, ShopCategory.SPAWN_EGG),
    POLAR_BEAR_SPAWN_EGG(Material.POLAR_BEAR_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    RABBIT_SPAWN_EGG(Material.RABBIT_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    RAVAGER_SPAWN_EGG(Material.RAVAGER_SPAWN_EGG, 15_000_000, 1_500, ShopCategory.SPAWN_EGG),

    SHEEP_SPAWN_EGG(Material.SHEEP_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    SHULKER_SPAWN_EGG(Material.SHULKER_SPAWN_EGG, 15_000_000, 1_500, ShopCategory.SPAWN_EGG),
    SILVERFISH_SPAWN_EGG(Material.SILVERFISH_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    SKELETON_SPAWN_EGG(Material.SKELETON_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    SKELETON_HORSE_SPAWN_EGG(Material.SKELETON_HORSE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    SLIME_SPAWN_EGG(Material.SLIME_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    SNOW_GOLEM_SPAWN_EGG(Material.SNOW_GOLEM_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    SPIDER_SPAWN_EGG(Material.SPIDER_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),

    SQUID_SPAWN_EGG(Material.SQUID_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    STRAY_SPAWN_EGG(Material.STRAY_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    TRADER_LLAMA_SPAWN_EGG(Material.TRADER_LLAMA_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    TURTLE_SPAWN_EGG(Material.TURTLE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    VILLAGER_SPAWN_EGG(Material.VILLAGER_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),

    WANDERING_TRADER_SPAWN_EGG(Material.WANDERING_TRADER_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    WITCH_SPAWN_EGG(Material.WITCH_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    WITHER_SKELETON_SPAWN_EGG(Material.WITHER_SKELETON_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    WOLF_SPAWN_EGG(Material.WOLF_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    ZOMBIE_SPAWN_EGG(Material.ZOMBIE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),
    ZOMBIE_HORSE_SPAWN_EGG(Material.ZOMBIE_HORSE_SPAWN_EGG, 1_000_000, 1_000, ShopCategory.SPAWN_EGG),


    // ORE

    AMETHYST_SHARD(Material.AMETHYST_SHARD, 15, 10, ShopCategory.ORE),

    FLINT(Material.FLINT, -1, 3, ShopCategory.ORE),
    COAL(Material.COAL, 3, 2, ShopCategory.ORE),
    QUARTZ(Material.QUARTZ, 1.2f, -1, ShopCategory.ORE),
    LAPIS_LAZULI(Material.LAPIS_LAZULI, 15, 5, ShopCategory.ORE),
    REDSTONE(Material.REDSTONE, 1.25f, 1, ShopCategory.ORE),
    COPPER_INGOT(Material.COPPER_INGOT, 15, 3, ShopCategory.ORE),
    IRON_INGOT(Material.IRON_INGOT, 20, 10, ShopCategory.ORE),
    GOLD_INGOT(Material.GOLD_INGOT, 25, 15, ShopCategory.ORE),
    DIAMOND(Material.DIAMOND, 35, 20, ShopCategory.ORE),
    NETHERITE_SCRAP(Material.NETHERITE_SCRAP, 40, -1, ShopCategory.ORE),
    NETHERITE_INGOT(Material.NETHERITE_INGOT, -1, 125, ShopCategory.ORE),
    EMERALD(Material.EMERALD, 50, 40, ShopCategory.ORE);


    private final float buyPrice, sellPrice;
    private final Material material;
    private final ShopCategory shopCategory;

    ItemShop(Material material, float buyPrice, float sellPrice, ShopCategory shopCategory) {
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

    public float getBuyPrice() {
        return buyPrice;
    }

    public float getSellPrice() {
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
