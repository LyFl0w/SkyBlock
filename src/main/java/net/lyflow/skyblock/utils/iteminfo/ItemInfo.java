package net.lyflow.skyblock.utils.iteminfo;

import org.bukkit.Material;

public class ItemInfo extends UniqueItemInfo {

    protected final String[] description;

    public ItemInfo(int slot, Material material, String name, String... description) {
        super(slot, material, name);
        this.description = description;
    }

    public String[] getDescription() {
        return description;
    }

    public static ItemInfo of(int slot, Material material, String name, String... description) {
        return new ItemInfo(slot, material, name, description);
    }

}
