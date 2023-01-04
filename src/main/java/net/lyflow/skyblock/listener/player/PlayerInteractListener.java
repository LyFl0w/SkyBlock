package net.lyflow.skyblock.listener.player;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PlayerInteractListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getHand() != EquipmentSlot.HAND) return;

        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final Block block = event.getClickedBlock();
        final ItemStack currentItem = event.getItem();

        if(action == Action.RIGHT_CLICK_BLOCK) {
            if(block.getBlockData() instanceof final Ageable ageable) {
                if(ageable instanceof Bamboo || ageable instanceof CaveVines || ageable instanceof Cocoa || ageable instanceof Fire || ageable instanceof MangrovePropagule) return;

                if((currentItem != null && currentItem.getType() == Material.BONE_MEAL) || ageable.getAge() != ageable.getMaximumAge()) return;

                final ArrayList<ItemStack> drops = new ArrayList<>(block.getDrops());
                Material materialToRemove = null;
                switch(block.getType()) {
                    case WHEAT -> materialToRemove = Material.WHEAT_SEEDS;
                    case BEETROOTS -> materialToRemove = Material.BEETROOT_SEEDS;
                    case CARROTS -> materialToRemove = Material.CARROT;
                    case POTATOES -> {
                        if(drops.stream().filter(itemStack -> itemStack.getType() == Material.POTATO).count() > 0) materialToRemove = Material.POTATO;
                    }
                }
                if(materialToRemove == null) return;

                for(int i=0; i<drops.size(); i++) {
                    final ItemStack itemStack = drops.get(i);
                    if(itemStack.getType() == materialToRemove) {
                        final int newAmount = itemStack.getAmount()-1;
                        if(newAmount > 0) itemStack.setAmount(newAmount);
                        else drops.remove(i);
                        break;
                    }
                }

                ageable.setAge(0);
                block.setBlockData(ageable);

                final Location location = block.getLocation();
                final World world = location.getWorld();

                drops.forEach(itemStack -> world.dropItemNaturally(location, itemStack));
                world.playSound(location, Sound.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0f, 1.0f);
            }
        }
    }

}
