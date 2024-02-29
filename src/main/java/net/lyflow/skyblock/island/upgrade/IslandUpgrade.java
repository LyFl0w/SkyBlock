package net.lyflow.skyblock.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import net.lyflow.skyblock.utils.iteminfo.ItemInfo;
import net.lyflow.skyblock.utils.iteminfo.UniqueItemInfo;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public abstract class IslandUpgrade {

    protected final SkyBlock skyBlock;

    protected final IslandUpgradeStatusManager islandUpgradeStatusManager;

    protected final int id;
    protected final Type type;
    protected final LevelUpgrade levelUpgrade;
    protected final ItemInfo itemInfo;

    protected IslandUpgrade(SkyBlock skyBlock, int id, Type type, LevelUpgrade levelUpgrade, ItemInfo itemInfo) {
        if (type.isUniqueEvent && !skyBlock.getIslandUpgradeManager().getIslandUpgradesByType(type).isEmpty())
            throw new IllegalCallerException("The upgrade " + type.name() + " can't be instanced more than once !");

        this.skyBlock = skyBlock;

        this.id = id;
        this.type = type;
        this.levelUpgrade = levelUpgrade;
        this.itemInfo = itemInfo;

        this.islandUpgradeStatusManager = new IslandUpgradeStatusManager(this);
    }

    protected IslandUpgrade(SkyBlock skyBlock, int id, Type type, LevelUpgrade levelUpgrade, UniqueItemInfo itemInfo) {
        this(skyBlock, id, type, levelUpgrade, itemInfo.toItemInfo());
        if (!type.isUniqueEvent)
            throw new IllegalCallerException("The upgrade " + type.name() + " need to be instanced with UniqueItemInfo !");
    }

    public int getID() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public LevelUpgrade getLevelUpgrade() {
        return levelUpgrade;
    }

    public IslandUpgradeStatusManager getIslandUpgradeStatusManager() {
        return islandUpgradeStatusManager;
    }

    public ItemStack getRepresentation(IslandUpgradeStatus upgradeStatus, int level) {
        final ItemBuilder itemBuilder = itemInfo.getItemBuildRepresentation();

        if (!levelUpgrade.isOneLevel()) {
            if (level == 0) {
                itemBuilder.setLore(getDescription());

                if (upgradeStatus.isEnable())
                    itemBuilder.addVisualEnchant();
            } else {
                itemBuilder.setLore(levelUpgrade.getDescriptions(level));

                if (level == upgradeStatus.getCurrentLevel())
                    itemBuilder.addVisualEnchant();
            }

        } else {
            itemBuilder.setLore(getDescription());

            if (upgradeStatus.isEnable())
                itemBuilder.addVisualEnchant();
        }

        return itemBuilder.toItemStack();
    }

    public int getSlot() {
        return itemInfo.getSlot();
    }

    public Material getMaterial() {
        return itemInfo.getMaterial();
    }

    public String getName() {
        return itemInfo.getName();
    }

    public String[] getDescription() {
        return itemInfo.getDescription();
    }

    public enum Type {
        COBBLESTONE_GENERATOR(ItemInfo.of(0, Material.COBBLESTONE, "Cobblestone Generator Upgrade", ""), false),
        TNT_DROP_RATE(ItemInfo.of(1, Material.TNT, "Tnt Drop Rate Upgrade", ""), true);

        private final ItemInfo itemInfo;
        private final boolean isUniqueEvent;

        Type(ItemInfo itemInfo, boolean isUniqueEvent) {
            this.itemInfo = itemInfo;
            this.isUniqueEvent = isUniqueEvent;
        }

        @Nullable
        public static Type getTypeBySlot(int slot) {
            for (Type type : values()) {
                if (type.getSlot() == slot)
                    return type;
            }
            return null;
        }

        public int getSlot() {
            return itemInfo.getSlot();
        }

        public boolean isUniqueEvent() {
            return isUniqueEvent;
        }

        public ItemStack getRepresentation(IslandUpgradeManager islandUpgradeManager, int islandID) {
            final ItemBuilder itemBuilder = itemInfo.getItemBuildRepresentation();

            if (islandUpgradeManager.getIslandUpgradesByType(this).stream().parallel().anyMatch(islandUpgrade
                    -> islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID).isEnable())) {
                itemBuilder.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemBuilder.addEnchant(Enchantment.VANISHING_CURSE, 1);
            }

            return itemBuilder.toItemStack();
        }

        public static int getMaxSlot() {
            final Optional<Type> optional = Arrays.stream(values()).parallel().max(Comparator.comparingInt(Type::getSlot));
            if (optional.isEmpty()) throw new IllegalCallerException("There is no slot for upgrade !");
            return optional.get().getSlot();
        }
    }

}
