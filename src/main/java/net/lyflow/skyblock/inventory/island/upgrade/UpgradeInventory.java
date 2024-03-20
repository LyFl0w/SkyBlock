package net.lyflow.skyblock.inventory.island.upgrade;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.request.island.IslandRequest;
import net.lyflow.skyblock.event.island.upgrade.*;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatusManager;
import net.lyflow.skyblock.island.upgrade.LevelUpgradeManager;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.builder.InventoryBuilder;
import net.lyflow.skyblock.utils.builder.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

public class UpgradeInventory {

    private UpgradeInventory() {
        throw new IllegalStateException("Inventory class");
    }

    public static Inventory getTypeUpgradeInventory(IslandUpgradeManager islandUpgradeManager, int islandID) {
        final InventoryBuilder inventoryBuilder = new InventoryBuilder((IslandUpgrade.Type.getMaxSlot() / 9) * 9 + 9, "§6Upgrade");
        Arrays.stream(IslandUpgrade.Type.values()).parallel().forEach(type ->
                inventoryBuilder.setItem(type.getSlot(), type.getRepresentation(islandUpgradeManager, islandID)));
        return inventoryBuilder.toInventory();
    }

    public static Inventory getLevelUpgradeInventory(IslandUpgradeManager islandUpgradeManager, int upgradeID, int islandID) {
        final IslandUpgrade islandUpgrade = islandUpgradeManager.getIslandUpgradesByID(upgradeID);
        final LevelUpgradeManager levelUpgradeManager = islandUpgrade.getLevelUpgradeManager();

        if (levelUpgradeManager.isOneLevel())
            throw new IllegalArgumentException("There is no inventory for upgrade " + islandUpgrade.getName());

        final IslandUpgradeStatus status = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);
        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Upgrade/Level/" + upgradeID);

        for (int level = 1; level <= levelUpgradeManager.getMaxLevel(); level++) {
            inventoryBuilder.setItem(levelUpgradeManager.getSlot(level), islandUpgrade.getRepresentation(status, level));
        }
        inventoryBuilder.setItem(0, new ItemBuilder(Material.PAPER).setName("Back").toItemStack());

        return inventoryBuilder.toInventory();
    }

    public static Inventory getDeclinationUpgradeInventory(IslandUpgradeManager islandUpgradeManager, IslandUpgrade.Type type, int islandID) {
        if (type.isUniqueEvent())
            throw new IllegalArgumentException("Type Upgrade " + type.name() + " is unique !");

        final InventoryBuilder inventoryBuilder = new InventoryBuilder(9, "§6Upgrade/" + type.getSlot());
        islandUpgradeManager.getIslandUpgradesByType(type).stream().parallel().forEach(islandUpgrade -> {
            final IslandUpgradeStatus islandUpgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);
            inventoryBuilder.setItem(islandUpgrade.getSlot(), islandUpgrade.getRepresentation(islandUpgradeStatus, 0));
        });
        inventoryBuilder.setItem(0, new ItemBuilder(Material.PAPER).setName("Back").toItemStack());

        return inventoryBuilder.toInventory();
    }

    public static void inventoryTypeUpgradeEvent(SkyBlock skyBlock, InventoryClickEvent event, Player player) {
        event.setCancelled(true);

        try {
            final int islandID = new IslandRequest(skyBlock.getDatabase(), true).getIslandID(player.getUniqueId());

            final IslandUpgradeManager islandUpgradeManager = skyBlock.getIslandUpgradeManager();
            final IslandUpgrade.Type type = IslandUpgrade.Type.getTypeBySlot(event.getSlot());

            if (type.isUniqueEvent()) {
                final IslandUpgrade islandUpgrade = islandUpgradeManager.getUniqueIslandUpgradesByType(type);
                final LevelUpgradeManager levelUpgradeManager = islandUpgrade.getLevelUpgradeManager();

                if (levelUpgradeManager.isOneLevel()) {
                    final IslandUpgradeStatus islandUpgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);
                    skyBlock.getServer().getPluginManager().callEvent(
                            (!islandUpgradeStatus.isBuy())
                                    ? new BuyIslandUpgradeEvent(skyBlock, player, islandUpgrade)
                                    : new ToggleIslandUpgradeEvent(skyBlock, player, islandUpgrade,
                                    islandUpgradeStatus.getCurrentLevel(), islandUpgradeStatus.getLastBuyLevel())
                    );
                    return;
                }

                player.openInventory(getLevelUpgradeInventory(islandUpgradeManager, islandUpgrade.getID(), islandID));
                return;
            }

            player.openInventory(getDeclinationUpgradeInventory(islandUpgradeManager, type, islandID));
        } catch (SQLException e) {
            skyBlock.getLogger().log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void inventoryLevelUpgradeEvent(SkyBlock skyBlock, InventoryClickEvent event, int upgradeID, Player player, ItemStack item) {
        event.setCancelled(true);

        final IslandUpgradeManager islandUpgradeManager = skyBlock.getIslandUpgradeManager();
        final IslandUpgrade islandUpgrade = islandUpgradeManager.getIslandUpgradesByID(upgradeID);
        final IslandUpgrade.Type type = islandUpgrade.getType();

        try {
            final int islandID = new IslandRequest(skyBlock.getDatabase(), true).getIslandID(player.getUniqueId());

            if (item.getType() == Material.PAPER) {
                player.openInventory(!type.isUniqueEvent()
                        ? getDeclinationUpgradeInventory(islandUpgradeManager, type, islandID)
                        : getTypeUpgradeInventory(islandUpgradeManager, islandID)
                );
                return;
            }

            final IslandUpgradeStatusManager islandUpgradeStatusManager = islandUpgrade.getIslandUpgradeStatusManager();
            final IslandUpgradeStatus islandUpgradeStatus = islandUpgradeStatusManager.getIslandUpgradeStatus(islandID);
            final LevelUpgradeManager levelUpgradeManager = islandUpgrade.getLevelUpgradeManager();

            final int targetLevel = levelUpgradeManager.getLevelForSlot(event.getSlot());
            final int currentLevel = islandUpgradeStatus.getCurrentLevel();
            final int lastBuyLevel = islandUpgradeStatus.getLastBuyLevel();

            if (!islandUpgradeStatus.isBuy()) {
                // DETECTER LE PREMIER ACHAT (LVL1)
                if (targetLevel == 1) {
                    skyBlock.getServer().getPluginManager().callEvent(new BuyIslandUpgradeEvent(skyBlock, player, islandUpgrade));
                    return;
                }
                // DETECTER SI CEST UNE FRAUDE DACHAT
                player.sendMessage("§cVous devez d'abord acheter les upgrades précédents !");
                return;
            }

            // DETECTER SI CEST UNE ACTIVATION/DESACTIVATION DE L'UPGRADE
            if (targetLevel == currentLevel) {
                skyBlock.getServer().getPluginManager().callEvent(new ToggleIslandUpgradeEvent(skyBlock, player, islandUpgrade, currentLevel, targetLevel));
                return;
            }

            // DETECTER SI CEST UN DOWNGRADE DE LA SELECTION
            if (targetLevel < currentLevel) {
                skyBlock.getServer().getPluginManager().callEvent(islandUpgradeStatus.isEnable()
                        ? new LevelDownIslandUpgradeEvent(skyBlock, player, islandUpgrade, currentLevel, targetLevel)
                        : new ToggleIslandUpgradeEvent(skyBlock, player, islandUpgrade, currentLevel, targetLevel)
                );
                return;
            }

            if (targetLevel == lastBuyLevel + 1) {
                new BuyIslandLevelUpgradeEvent(skyBlock, player, islandUpgrade, currentLevel, targetLevel);
                return;
            }

            if (targetLevel > lastBuyLevel + 1) {
                // DETECTER SI CEST UNE FRAUDE DACHAT
                player.sendMessage("§cVous devez d'abord acheter les upgrades précédents !");
                return;
            }

            // CEST UN UPGRADE
            skyBlock.getServer().getPluginManager().callEvent(islandUpgradeStatus.isEnable()
                    ? new LevelUpIslandUpgradeEvent(skyBlock, player, islandUpgrade, currentLevel, targetLevel)
                    : new ToggleIslandUpgradeEvent(skyBlock, player, islandUpgrade, currentLevel, targetLevel)
            );
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void inventoryTypeDeclinationUpgradeEvent(SkyBlock skyBlock, InventoryClickEvent event, int index, Player player, ItemStack item) {
        event.setCancelled(true);

        final IslandUpgradeManager islandUpgradeManager = skyBlock.getIslandUpgradeManager();
        try {
            final int islandID = new IslandRequest(skyBlock.getDatabase(), true).getIslandID(player.getUniqueId());

            if (item.getType() == Material.PAPER) {
                player.openInventory(getTypeUpgradeInventory(islandUpgradeManager, islandID));
                return;
            }

            final IslandUpgrade.Type type = IslandUpgrade.Type.getTypeBySlot(index);
            final IslandUpgrade islandUpgrade = islandUpgradeManager.getIslandUpgradesBySlot(type, event.getSlot());

            if (islandUpgrade.getLevelUpgradeManager().isOneLevel()) {
                final IslandUpgradeStatus islandUpgradeStatus = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);
                skyBlock.getServer().getPluginManager().callEvent(
                        (!islandUpgradeStatus.isBuy())
                                ? new BuyIslandUpgradeEvent(skyBlock, player, islandUpgrade)
                                : new ToggleIslandUpgradeEvent(skyBlock, player, islandUpgrade,
                                islandUpgradeStatus.getCurrentLevel(), islandUpgradeStatus.getLastBuyLevel())
                );
                return;
            }

            player.openInventory(getLevelUpgradeInventory(islandUpgradeManager, islandUpgrade.getID(), islandID));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}