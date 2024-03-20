package net.lyflow.skyblock.loader.island.upgrade;

import com.google.gson.annotations.SerializedName;
import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import net.lyflow.skyblock.loader.island.upgrade.mod.CobblestoneGeneratorData;
import net.lyflow.skyblock.utils.iteminfo.ItemInfo;
import net.lyflow.skyblock.utils.iteminfo.UniqueItemInfo;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class IslandUpgradeData {

    private final int id;
    private final int slot;
    private final String type;
    private final String material;
    private final String name;
    private final List<String> description;
    private List<LevelUpgrade> upgrades;
    @SerializedName("default")
    private final Map<String, Object> data;

    public IslandUpgradeData(int id, int slot, String type, String material, String name, List<String> description, List<LevelUpgrade> upgrades, Map<String, Object> data) {
        this.id = id;
        this.slot = slot;
        this.type = type;
        this.material = material;
        this.name = name;
        this.description = description;
        this.upgrades = upgrades;
        this.data = data;
    }

    public IslandUpgradeData(int id, int slot, String type, String material, String name, List<String> description, List<LevelUpgrade> upgrades) {
        this(id, slot, type, material, name, description, upgrades, null);
    }

    public IslandUpgrade toUpgrade(SkyBlock skyBlock) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        final IslandUpgrade.Type reelType = IslandUpgrade.Type.getTypeByName(type);
        final boolean isUnique = reelType.isUniqueEvent();
        final Material reelMaterial = Registry.MATERIAL.get(Objects.requireNonNull(NamespacedKey.fromString(material)));

        final IslandUpgrade islandUpgrade;
        final Constructor<?> constructor = reelType.getIslandUpgradeClass().getConstructors()[0];
        final int parameterCount = constructor.getParameterCount();
        final boolean hasData = parameterCount == 5;

        if (hasData && data == null)
            throw new IllegalArgumentException(name + " island upgrade needs default parameters !");

        if (!hasData && data != null)
            throw new IllegalArgumentException(name + " island upgrade don't have default parameters !");

        // rewrite upgrade ( Example : Cobblestone Generator -> (Cobblestone Generator Data -> Cobblestone Generator Upgrade) )
        rewriteUpgrade(reelType);

        if (isUnique) {
            final UniqueItemInfo itemInfo = UniqueItemInfo.of(slot, reelMaterial, name);

            if (hasData) {
                islandUpgrade = (IslandUpgrade) constructor.newInstance(skyBlock, id, upgrades, data, itemInfo);
            } else {
                islandUpgrade = (IslandUpgrade) constructor.newInstance(skyBlock, id, upgrades, itemInfo);
            }
        } else {
            final ItemInfo itemInfo = ItemInfo.of(slot, reelMaterial, name, (description == null ? new String[]{} : description.toArray(new String[0])));
            System.out.println("description of " + name + " : " + Arrays.toString(description == null ? new String[]{} : description.toArray(new String[0])));
            if (hasData) {
                islandUpgrade = (IslandUpgrade) constructor.newInstance(skyBlock, id, upgrades, data, itemInfo);
            } else {
                islandUpgrade = (IslandUpgrade) constructor.newInstance(skyBlock, id, upgrades, itemInfo);
            }
        }

        return islandUpgrade;
    }

    private void rewriteUpgrade(IslandUpgrade.Type type) {
        final List<LevelUpgrade> rewriteUpgrades = new ArrayList<>();

        switch (type) {
            case COBBLESTONE_GENERATOR -> {
                for (LevelUpgrade levelUpgrade : upgrades) {
                    final Map<String, Object> rewriteData = new HashMap<>();
                    for (Map.Entry<String, Object> entry : levelUpgrade.getData().entrySet())
                        rewriteData.put(entry.getKey(), ((CobblestoneGeneratorData) entry.getValue()).toGenerator());
                    rewriteUpgrades.add(new LevelUpgrade(levelUpgrade, rewriteData));
                }
            }
            default -> {
                return;
            }
        }

        if (!rewriteUpgrades.isEmpty())
            this.upgrades = rewriteUpgrades;
    }

}
