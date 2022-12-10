package net.lyflow.skyblock.utils.builder;

import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionData;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ItemBuilder implements Cloneable{

    private final ItemStack itemStack;

    public ItemBuilder(Material material){
        this(material, 1);
    }

    public ItemBuilder(Material material, ItemFlag... itemFlags){
        this(material, 1, itemFlags);
    }

    public ItemBuilder(Material material, int amount, ItemFlag... itemFlags){
        this(material, amount);
        addItemFlags(itemFlags);
    }

    public ItemBuilder(Material material, int amount){
        this(new ItemStack(material, amount));
    }

    @Deprecated
    public ItemBuilder(Material material, byte meta){
        this(new ItemStack(material, 1, meta));
    }

    @Deprecated
    public ItemBuilder(Material material, int amount, short meta){
        this(new ItemStack(material, amount, meta));
    }

    public ItemBuilder(ItemStack itemStack){
        this.itemStack = itemStack;
    }

    @Override
    protected ItemBuilder clone() throws CloneNotSupportedException{
        return (ItemBuilder) super.clone();
    }

    public String getName(){
        return getItemMeta().getDisplayName();
    }

    public ItemBuilder setDamage(int damage){
        final Damageable im = (Damageable) getItemMeta();
        im.setDamage(damage);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setName(String name){
        final ItemMeta im = getItemMeta();
        im.setDisplayName(name);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level){
        itemStack.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench){
        itemStack.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(OfflinePlayer offlinePlayer){
        Validate.isTrue(itemStack.getType() == Material.PLAYER_HEAD, "The item must be a player's head");
        final SkullMeta im = (SkullMeta) getItemMeta();
        im.setOwningPlayer(offlinePlayer);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level){
        final ItemMeta im = getItemMeta();
        im.addEnchant(ench, level, true);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore){
        final ItemMeta im = getItemMeta();
        im.setLore(lore);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setPotionData(PotionData potionData){
        Validate.isTrue((itemStack.getType() == Material.POTION || itemStack.getType() == Material.SPLASH_POTION || itemStack.getType() == Material.SPLASH_POTION), "The item must be a potion");
        final PotionMeta potionMeta = (PotionMeta) getItemMeta();
        potionMeta.setBasePotionData(potionData);
        itemStack.setItemMeta(potionMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore){
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags){
        final ItemMeta im = getItemMeta();
        im.addItemFlags(itemFlags);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder addTextPage(int page, String text){
        Validate.isTrue((itemStack.getType() == Material.WRITABLE_BOOK || itemStack.getType() == Material.WRITTEN_BOOK), "The item must be a book");
        final BookMeta bm = (BookMeta) getItemMeta();
        bm.setPage(page, bm.getPage(page) + text);
        itemStack.setItemMeta(bm);
        return this;
    }

    public ItemBuilder setUnbreakable(){
        final ItemMeta im = getItemMeta();
        im.setUnbreakable(true);
        itemStack.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color){
        Validate.isTrue((itemStack.getType() == Material.LEATHER_BOOTS || itemStack.getType() == Material.LEATHER_CHESTPLATE || itemStack.getType() == Material.LEATHER_HELMET || itemStack.getType() == Material.LEATHER_LEGGINGS || itemStack.getType() == Material.LEATHER_HORSE_ARMOR), "The item must be a book");
        final LeatherArmorMeta im = (LeatherArmorMeta) getItemMeta();
        im.setColor(color);
        itemStack.setItemMeta(im);
        return this;
    }

    public boolean isItemDye(){
        return switch(itemStack.getType()){
            case BLACK_DYE, BLUE_DYE, BROWN_DYE, CYAN_DYE, GRAY_DYE, GREEN_DYE, LIGHT_BLUE_DYE, LIME_DYE, LIGHT_GRAY_DYE, MAGENTA_DYE, ORANGE_DYE, PINK_DYE, PURPLE_DYE, RED_DYE, WHITE_DYE, YELLOW_DYE -> true;
            default -> false;
        };
    }

    @Nonnull
    private ItemMeta getItemMeta(){
        return Objects.requireNonNull(itemStack.getItemMeta());
    }

    public ItemStack toItemStack(){
        return itemStack;
    }
}