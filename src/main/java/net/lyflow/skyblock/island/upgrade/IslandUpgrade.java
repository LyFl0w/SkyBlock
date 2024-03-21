package net.lyflow.skyblock.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.mod.CobblestoneGeneratorUpgrade;
import net.lyflow.skyblock.island.upgrade.mod.TntDropRateUpgrade;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import net.lyflow.skyblock.utils.iteminfo.ItemInfo;
import net.lyflow.skyblock.utils.iteminfo.UniqueItemInfo;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class IslandUpgrade {

    public final SkyBlock skyBlock;
    public final LevelUpgradeManager levelUpgradeManager;
    public final IslandUpgradeStatusManager islandUpgradeStatusManager;

    public final int id;
    public final Type type;
    public final ItemInfo itemInfo;

    public IslandUpgrade(SkyBlock skyBlock, int id, Type type, List<? extends LevelUpgrade> levelUpgrades, ItemInfo itemInfo) {
        if (type.isUniqueEvent && !skyBlock.getIslandUpgradeManager().getIslandUpgradesByType(type).isEmpty())
            throw new IllegalCallerException("The upgrade " + type.name() + " can't be instanced more than once !");

        this.skyBlock = skyBlock;

        this.id = id;
        this.type = type;
        this.itemInfo = itemInfo;

        this.levelUpgradeManager = new LevelUpgradeManager(levelUpgrades);
        this.islandUpgradeStatusManager = new IslandUpgradeStatusManager(this);
    }

    public IslandUpgrade(SkyBlock skyBlock, int id, Type type, List<? extends LevelUpgrade> levelUpgrades, UniqueItemInfo itemInfo) {
        this(skyBlock, id, type, levelUpgrades, itemInfo.toItemInfo());
        if (!type.isUniqueEvent)
            throw new IllegalCallerException("The upgrade " + type.name() + " need to be instanced with ItemInfo and not with UniqueItemInfo !");
    }

    public int getID() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public LevelUpgradeManager getLevelUpgradeManager() {
        return levelUpgradeManager;
    }

    public IslandUpgradeStatusManager getIslandUpgradeStatusManager() {
        return islandUpgradeStatusManager;
    }

    public ItemStack getRepresentation(IslandUpgradeStatus upgradeStatus, int level) {
        final ItemBuilder itemBuilder = itemInfo.getItemBuildRepresentation();

        if (!levelUpgradeManager.isOneLevel()) {
            if (level == 0) {
                itemBuilder.setLore(getDescription());

                if (upgradeStatus.isEnable())
                    itemBuilder.addVisualEnchant();
            } else {
                itemBuilder.setLore(levelUpgradeManager.getDescriptions(level));

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
        COBBLESTONE_GENERATOR(
                ItemInfo.of(0, Material.COBBLESTONE, "Cobblestone Generator Upgrade", ""),
                false, CobblestoneGeneratorUpgrade.class
        ),

        TNT_DROP_RATE(
                ItemInfo.of(1, Material.TNT, "Tnt Drop Rate Upgrade", ""),
                true, TntDropRateUpgrade.class
        );

        private final ItemInfo itemInfo;
        private final boolean isUniqueEvent;
        private final Class<? extends IslandUpgrade> islandUpgradeClass;

        Type(ItemInfo itemInfo, boolean isUniqueEvent, Class<? extends IslandUpgrade> islandUpgradeClass) {
            this.itemInfo = itemInfo;
            this.isUniqueEvent = isUniqueEvent;
            this.islandUpgradeClass = islandUpgradeClass;
        }

        public static Type getTypeBySlot(int slot) {
            for (Type type : values()) {
                if (type.getSlot() == slot)
                    return type;
            }
            throw new IllegalArgumentException("the type in slot " + slot + " doesn't exist !");
        }

        public static Type getTypeByName(String name) {
            for (Type type : values()) {
                if (name.equals(type.name().toLowerCase()))
                    return type;
            }
            throw new IllegalArgumentException("the type " + name + " doesn't exist !");
        }

        public int getSlot() {
            return itemInfo.getSlot();
        }

        public boolean isUniqueEvent() {
            return isUniqueEvent;
        }

        public Class<? extends IslandUpgrade> getIslandUpgradeClass() {
            return islandUpgradeClass;
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
