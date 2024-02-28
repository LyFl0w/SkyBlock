package net.lyflow.skyblock.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class BlockUtils {

    public static Block getSourceOfBlock(Block block) {
        final Material material = block.getType();
        if (!(material == Material.LAVA || material == Material.WATER))
            throw new IllegalCallerException("The block " + block.getType() + " is not a liquid");

        final int level = ((Levelled) block.getBlockData()).getLevel();
        return (level == 0) ? block : recurrenceFluid(material, block.getLocation(), level);
    }

    @Nullable
    public static Block blockIsNextTo(Block block, Material nextTo) {
        for (Way way : Way.values()) {
            final Block nextToBlock = block.getLocation().clone().add(way.getVector()).getBlock();
            if (nextToBlock.getType() == nextTo) return nextToBlock;
        }
        return null;
    }

    @Nullable
    public static Block isNextToLiquidSource(Location location, Material nextTo) {
        if (!(nextTo == Material.LAVA || nextTo == Material.WATER))
            throw new IllegalCallerException("The block " + nextTo + " is not a liquid");
        for (Way way : Way.values()) {
            final Block nextToBlock = location.clone().add(way.getVector()).getBlock();
            if (nextToBlock.getType() == nextTo && ((Levelled) nextToBlock.getBlockData()).getLevel() == 0)
                return nextToBlock;
        }
        return null;
    }

    @Nullable
    private static Block recurrenceFluid(Material needToBe, Location location, int levelBefore) {
        Location saveLoc = null;
        for (Way way : Way.values()) {
            final Location newLoc = location.clone().add(way.getVector());
            final Block block = newLoc.getBlock();
            if (block.getType() == needToBe) {
                final int level = ((Levelled) block.getBlockData()).getLevel();
                if (level == 0) return block;
                if (way == Way.UP || levelBefore < level) {
                    saveLoc = newLoc;
                    levelBefore = level;
                }
            }
        }
        if (saveLoc == null) return null;
        return recurrenceFluid(needToBe, saveLoc, levelBefore);
    }

    private enum Way {
        LEFT_X(0, new Vector(-1, 0, 0)),
        RIGHT_X(1, new Vector(1, 0, 0)),
        LEFT_Z(2, new Vector(0, 0, 1)),
        RIGHT_Z(3, new Vector(0, 0, -1)),
        UP(4, new Vector(0, 1, 0));

        private final int id;
        private final Vector vector;

        Way(int id, Vector vector) {
            this.id = id;
            this.vector = vector;
        }

        public int getID() {
            return id;
        }

        public Vector getVector() {
            return vector;
        }

    }

}
