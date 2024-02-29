package net.lyflow.skyblock.island.upgrade.mod;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.island.upgrade.IslandUpgrade;
import net.lyflow.skyblock.island.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.island.upgrade.LevelUpgrade;
import net.lyflow.skyblock.island.upgrade.LevelUpgradeKey;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.utils.BlockUtils;
import net.lyflow.skyblock.utils.LocationUtils;
import net.lyflow.skyblock.utils.iteminfo.ItemInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CobblestoneGeneratorUpgrade extends IslandUpgrade {

    public CobblestoneGeneratorUpgrade(SkyBlock skyBlock, int id, List<LevelUpgrade> upgrades, ItemInfo itemInfo) {
        super(skyBlock, id, Type.COBBLESTONE_GENERATOR, upgrades, itemInfo);
    }

    public static class Generator {

        protected record MaterialEntry(Material material, double probability) {
        }

        private final Material materialActivation;
        private final List<MaterialEntry> summedMaterialProbability;
        private final Map<Material, Double> initialMaterialProbability;
        private final Random random;

        public Generator(Material materialActivation, Material defaultMaterial, Map<Material, Double> materialProbability) {
            this.materialActivation = materialActivation;
            this.random = new Random();
            this.summedMaterialProbability = new ArrayList<>();
            this.initialMaterialProbability = materialProbability;
            validateAndInitProbabilities(defaultMaterial, materialProbability);
        }

        public Generator(Material materialActivation, Map<Material, Double> materialProbability) {
            this(materialActivation, Material.COBBLESTONE, materialProbability);
        }

        private void validateAndInitProbabilities(Material defaultMaterial, Map<Material, Double> materialProbability) {
            double totalProbability = 0.0;
            for (Map.Entry<Material, Double> entry : materialProbability.entrySet()) {
                final double probability = entry.getValue();
                if (probability <= 0 || probability >= 1)
                    throw new IllegalArgumentException("Probability must be between 0 and 1");

                totalProbability += probability;
                summedMaterialProbability.add(new MaterialEntry(entry.getKey(), totalProbability));
            }

            if (totalProbability > 1.0)
                throw new IllegalArgumentException("Total probability exceeds 1");

            final double rest = 1.0 - totalProbability;
            if (rest > 0) summedMaterialProbability.add(new MaterialEntry(defaultMaterial, rest));
        }

        public Material getRandomMaterial() {
            final double randomNumber = random.nextDouble();
            for (MaterialEntry entry : summedMaterialProbability) {
                if (randomNumber < entry.probability)
                    return entry.material;
            }

            return summedMaterialProbability.get(summedMaterialProbability.size() - 1).material;
        }

        protected Map<Material, Double> getMaterialProbability() {
            return initialMaterialProbability;
        }

        public enum PreGenerator {
            UPGRADE_1(new CobblestoneGeneratorUpgrade.Generator(
                    Material.COAL_BLOCK,
                    Map.of(
                            Material.COAL_ORE, 0.2,
                            Material.LAPIS_ORE, 0.2,
                            Material.REDSTONE_ORE, 0.3
                    )
            )),

            UPGRADE_1_2(new CobblestoneGeneratorUpgrade.Generator(
                    Material.COAL_BLOCK,
                    Map.of(
                            Material.COAL_ORE, 0.25,
                            Material.LAPIS_ORE, 0.25,
                            Material.REDSTONE_ORE, 0.32
                    )
            )),

            UPGRADE_1_3(new CobblestoneGeneratorUpgrade.Generator(
                    Material.COAL_BLOCK,
                    Map.of(
                            Material.COAL_ORE, 0.3,
                            Material.LAPIS_ORE, 0.3,
                            Material.REDSTONE_ORE, 0.4
                    )
            )),

            UPGRADE_2(new CobblestoneGeneratorUpgrade.Generator(
                    Material.IRON_BLOCK,
                    Map.of(
                            Material.COPPER_ORE, 0.3,
                            Material.IRON_ORE, 0.2,
                            Material.GOLD_ORE, 0.1,
                            Material.DIAMOND_ORE, 0.1,
                            Material.EMERALD_ORE, 0.01
                    )
            )),

            UPGRADE_3(new CobblestoneGeneratorUpgrade.Generator(
                    Material.QUARTZ_BLOCK, Material.NETHERRACK,
                    Map.of(
                            Material.NETHER_QUARTZ_ORE, 0.2,
                            Material.NETHER_GOLD_ORE, 0.3,
                            Material.SOUL_SAND, 0.1
                    )
            ));

            final Generator generator;

            PreGenerator(Generator generator) {
                this.generator = generator;
            }

            public Generator getGenerator() {
                return generator;
            }
        }
    }

    public static class ListenerEvent implements Listener {

        private final IslandUpgradeManager islandUpgradeManager;

        public ListenerEvent(IslandUpgradeManager islandUpgradeManager) {
            this.islandUpgradeManager = islandUpgradeManager;
        }

        @EventHandler
        public void onBlockSpread(BlockFormEvent event) {
            final BlockState cobbleState = event.getNewState();

            if (cobbleState.getType() != Material.COBBLESTONE) return;

            final Location blockLoc = cobbleState.getLocation();
            final String worldName = blockLoc.getWorld().getName();

            if (worldName.startsWith("skyblock-map")) {
                final Block lavaSource = BlockUtils.isNextToLiquidSource(blockLoc, Material.LAVA);
                if (lavaSource == null) return;

                final Material upgradeBlockType = lavaSource.getLocation().add(0, -1, 0).getBlock().getType();
                final int islandID = LocationUtils.getIslandID(worldName);
                for (IslandUpgrade islandUpgrade : islandUpgradeManager.getIslandUpgradesByType(Type.COBBLESTONE_GENERATOR)) {
                    final IslandUpgradeStatus status = islandUpgrade.getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);
                    if (status.isEnable()) {
                        final LevelUpgrade levelUpgrade = islandUpgrade.getLevelUpgradeManager().getLevel(status.getCurrentLevel());
                        final Generator generator = levelUpgrade.getData(LevelUpgradeKey.GENERATOR);
                        if (generator.materialActivation == upgradeBlockType) {
                            cobbleState.setType(generator.getRandomMaterial());
                            break;
                        }
                    }
                }
            }
        }
    }
}
