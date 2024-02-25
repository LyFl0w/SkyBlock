package net.lyflow.skyblock.upgrade.mod;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.manager.IslandUpgradeManager;
import net.lyflow.skyblock.upgrade.IslandUpgrade;

import net.lyflow.skyblock.upgrade.IslandUpgradeStatus;
import net.lyflow.skyblock.utils.BlockUtils;
import net.lyflow.skyblock.utils.LocationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.util.*;

public class CobblestoneGeneratorUpgrade extends IslandUpgrade {

    private static final Map<Material, Generator> MATERIAL_INTEGER_MAP = new EnumMap<>(Material.class);

    public CobblestoneGeneratorUpgrade(SkyBlock skyBlock, Generator generator, float price, int slot, Material materialRepresentation, Material material, String name, String... description) {
        super(skyBlock, generator.id, Type.COBBLESTONE_GENERATOR, price, slot, materialRepresentation, name, description);
        MATERIAL_INTEGER_MAP.put(material, generator);
    }

    public CobblestoneGeneratorUpgrade(SkyBlock skyBlock, Generator generator, float price, int slot, Material material, String name, String... description) {
        this(skyBlock, generator, price, slot, material, material, name, description);
    }

    public CobblestoneGeneratorUpgrade(SkyBlock skyBlock, Generator.PreGenerator preGenerator, float price, int slot, Material materialRepresentation, Material material, String name, String... description) {
        this(skyBlock, preGenerator.generator, price, slot, materialRepresentation, material, name, description);
    }

    public CobblestoneGeneratorUpgrade(SkyBlock skyBlock, Generator.PreGenerator preGenerator, float price, int slot, Material material, String name, String... description) {
        this(skyBlock, preGenerator, price, slot, material, material, name, description);
    }

    public static class Generator {

        protected record MaterialEntry(Material material, double probability) {}

        private final int id;
        private final List<MaterialEntry> summedMaterialProbability;
        private final Map<Material, Double> initialMaterialProbability;
        private final Random random;

        public Generator(int id, Material defaultMaterial, Map<Material, Double> materialProbability) {
            this.id = id;
            this.random = new Random();
            this.summedMaterialProbability = new ArrayList<>();
            this.initialMaterialProbability = materialProbability;
            validateAndInitProbabilities(defaultMaterial, materialProbability);
        }

        public Generator(int id, Map<Material, Double> materialProbability) {
            this(id, Material.COBBLESTONE, materialProbability);
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

            if (totalProbability >= 1.0)
                throw new IllegalArgumentException("Total probability equals or exceeds 1");

            summedMaterialProbability.add(new MaterialEntry(defaultMaterial, 1.0 - totalProbability));
        }

        public Material getRandomMaterial() {
            final double randomNumber = random.nextDouble();
            for (MaterialEntry entry : summedMaterialProbability) {
                if (randomNumber < entry.probability)
                    return entry.material;
            }

            return summedMaterialProbability.get(summedMaterialProbability.size()-1).material;
        }

        protected Map<Material, Double> getMaterialProbability() {
            return initialMaterialProbability;
        }

        public enum PreGenerator {
            UPGRADE_1(new CobblestoneGeneratorUpgrade.Generator(0,
                    Map.of(
                            Material.COAL_ORE, 0.2,
                            Material.LAPIS_ORE, 0.2,
                            Material.REDSTONE_ORE, 0.3
                    )
            )),

            UPGRADE_2(new CobblestoneGeneratorUpgrade.Generator(1,
                    Map.of(
                            Material.COPPER_ORE, 0.3,
                            Material.IRON_ORE, 0.2,
                            Material.GOLD_ORE, 0.1,
                            Material.DIAMOND_ORE, 0.1,
                            Material.EMERALD_ORE, 0.01
                    )
            )),

            UPGRADE_3(new CobblestoneGeneratorUpgrade.Generator(2,
                    Material.NETHERRACK,
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

            if(cobbleState.getType() != Material.COBBLESTONE) return;

            final Location blockLoc = cobbleState.getLocation();
            final String worldName = blockLoc.getWorld().getName();

            if(worldName.startsWith("skyblock-map")) {
                final Block lavaSource = BlockUtils.isNextToLiquidSource(blockLoc, Material.LAVA);
                if(lavaSource == null) return;

                final Material upgradeBlockType = lavaSource.getLocation().add(0, -1, 0).getBlock().getType();
                if(!MATERIAL_INTEGER_MAP.containsKey(upgradeBlockType)) return;

                final Generator generator = MATERIAL_INTEGER_MAP.get(upgradeBlockType);
                final int islandID = LocationUtils.getIslandID(worldName);

                final IslandUpgradeStatus upgradeStatus = islandUpgradeManager.getIslandUpgradesByID(generator.id)
                        .getIslandUpgradeStatusManager().getIslandUpgradeStatus(islandID);

                if(!upgradeStatus.isEnable()) return;

                cobbleState.setType(generator.getRandomMaterial());
            }
        }
    }
}
