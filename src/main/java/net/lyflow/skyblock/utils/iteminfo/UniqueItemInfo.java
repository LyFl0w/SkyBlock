package net.lyflow.skyblock.utils.iteminfo;

import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;

public class UniqueItemInfo {

    protected final int slot;
    protected final Material material;
    protected final String name;

    public UniqueItemInfo(int slot, Material material, String name) {
        this.slot = slot;
        this.material = material;
        this.name = name;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public ItemBuilder getItemBuildRepresentation() {
        return new ItemBuilder(material)
                .setName(name);
    }

    public static UniqueItemInfo of(int slot, Material material, String name) {
        return new UniqueItemInfo(slot, material, name);
    }

    public ItemInfo toItemInfo() {
        return new ItemInfo(slot, material, name);
    }
}