package net.lyflow.skyblock.loader.minecraft;

import com.google.gson.annotations.SerializedName;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class ItemStackData {

    @SerializedName("item")
    private final String material;
    @SerializedName("count")
    private final int amount;
    private final List<EnchantmentData> enchantments;
    private final List<String> flags;

    public ItemStackData(String material, int amount, List<EnchantmentData> enchantments, List<String> flags) {
        this.material = material;
        this.amount = amount;
        this.enchantments = enchantments;
        this.flags = flags;
    }

    public ItemStack toItemStack() {
        final ItemBuilder itemBuilder = new ItemBuilder(Registry.MATERIAL.get(Objects.requireNonNull(NamespacedKey.fromString(material))), amount);

        if (enchantments != null) {
            for (EnchantmentData enchantmentData : enchantments)
                enchantmentData.apply(itemBuilder);
        }

        if (flags != null) {
            flags.forEach(flagName -> itemBuilder.addItemFlags(getItemFlagByName(flagName)));
        }

        return itemBuilder.toItemStack();
    }

    private ItemFlag getItemFlagByName(String name) {
        for (ItemFlag itemFlag : ItemFlag.values()) {
            if (name.equals(itemFlag.name().toLowerCase())) return itemFlag;
        }
        throw new IllegalArgumentException("the flag " + name + " doesn't exist !");
    }
}
