package net.lyflow.skyblock.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IslandUpgrade {

    protected final SkyBlock skyBlock;

    protected final IslandUpgradeStatusManager islandUpgradeStatusManager;

    protected final int id;
    protected final boolean save;

    protected final int slot;
    protected final Material material;
    protected final String name;
    protected final String[] description;

    protected final float price;

    protected final Type type;

    public IslandUpgrade(SkyBlock skyBlock, int id, Type type, boolean save, float price, int slot, Material material, String name, String... description) {
        this.skyBlock = skyBlock;

        this.id = id;
        this.type = type;
        this.save = save;

        this.slot = slot;
        this.material = material;
        this.name = name;
        this.description = description;

        this.price = price;

        this.islandUpgradeStatusManager = new IslandUpgradeStatusManager(this);
    }

    protected SkyBlock getSkyBlock() {
        return skyBlock;
    }

    public int getID() {
        return id;
    }

    public boolean isSave() {
        return save;
    }

    public ItemStack getRepresentation(int islandID) {
        return new ItemBuilder(material).setName(name).setLore(description).toItemStack();
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public IslandUpgradeStatusManager getIslandUpgradeStatusManager() {
        return islandUpgradeStatusManager;
    }

    public float getPrice() {
        return price;
    }

    public int getSlot() {
        return slot;
    }

    public enum Type {
        COBBLESTONE_GENERATOR,
        OTHER
    }

}
