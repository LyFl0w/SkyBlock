package net.lyflow.skyblock.upgrade.mod;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.upgrade.IslandUpgrade;

import org.bukkit.Material;

public class CobblestoneGeneratorUpgrade extends IslandUpgrade {

    public CobblestoneGeneratorUpgrade(SkyBlock skyBlock, int id, boolean save, float price, int slot, Material material, String name, String... description) {
        super(skyBlock, id, Type.COBBLESTONE_GENERATOR, save, price, slot, material, name, description);
    }

}
