package net.lyflow.skyblock.loader.item;

import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.util.Objects;

public class EnchantmentData {

    private final String enchantment;
    private final int level;

    public EnchantmentData(String enchantment, int level) {
        this.enchantment = enchantment;
        this.level = level;
    }

    public void apply(ItemBuilder itemBuilder) {
        itemBuilder.addUnsafeEnchantment(Registry.ENCHANTMENT.get(Objects.requireNonNull(NamespacedKey.fromString(enchantment))), level);
    }

}
