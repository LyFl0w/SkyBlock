package net.lyflow.skyblock.listener.block;

import net.lyflow.skyblock.utils.BlockUtils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;

import java.security.SecureRandom;

public class BlockSpreadListener implements Listener {

    @EventHandler
    public void onBlockSpread(BlockFormEvent event) {
        final BlockState cobbleState = event.getNewState();

        if(cobbleState.getType() != Material.COBBLESTONE) return;

        final Location blockLoc = cobbleState.getLocation();
        final String worldName = blockLoc.getWorld().getName();

        CobbleGen cobbleGen = CobbleGen.DEFAULT;

        if(worldName.startsWith("skyblock-map")) {
            final Block lavaSource = BlockUtils.isNextToLiquidSource(blockLoc, Material.LAVA);
            if(lavaSource == null) return;

            final Block upgradeBlock = lavaSource.getLocation().add(0, -1, 0).getBlock();
        }

        cobbleGen.setMaterial(cobbleState);

    }

    private enum CobbleGen {
        DEFAULT(null), COAL(Material.COAL_BLOCK), QUARTZ(Material.QUARTZ_BLOCK);

        private final Material material;

        CobbleGen(Material material) {
            this.material = material;
        }

        public void setMaterial(BlockState cobbleState) {
            switch(this) {
                case DEFAULT -> {
                    if(new SecureRandom().nextInt(20)+1 == 10) {
                        final int rdm = new SecureRandom().nextInt(33)+1;
                        Material material;
                        if(rdm <= 15) material = Material.IRON_ORE;
                        else if(rdm <= 25) material = Material.COPPER_ORE;
                        else if(rdm <= 30) material = Material.GOLD_ORE;
                        else if(rdm <= 32) material = Material.DIAMOND_ORE;
                        else material = Material.EMERALD_ORE;

                        cobbleState.setType(material);
                    }
                }

                case QUARTZ -> {
                    if(new SecureRandom().nextInt(20)+1 == 10) {
                        cobbleState.setType(Material.NETHER_QUARTZ_ORE);
                    } else {
                        cobbleState.setType(Material.NETHERRACK);
                    }
                }

                case COAL -> {
                    cobbleState.setType(Material.COBBLESTONE);
                }
            }
        }

    }

}
