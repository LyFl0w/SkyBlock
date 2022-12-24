package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.utils.StringUtils;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.List;

public class Reward {

    private final List<ItemStack> itemStacksAward;
    private final int level;

    public Reward(List<ItemStack> itemStacksAward, int level) {
        this.itemStacksAward = itemStacksAward;
        this.level = level;
    }

    public Reward(List<ItemStack> itemStacksAward) {
        this(itemStacksAward, 0);
    }

    public Reward(int level) {
        this(Collections.emptyList(), level);
    }

    public Reward() {
        this(Collections.emptyList(), 0);
    }

    public void getAward(Player player) {
        if(!itemStacksAward.isEmpty()) {
            final PlayerInventory playerInventory = player.getInventory();
            itemStacksAward.forEach(award -> {
                playerInventory.addItem(award);
                player.sendMessage("§a+"+award.getAmount()+" "+StringUtils.capitalizeSentence(award.getType().name(), "_", " "));
            });
        }

        if(level > 0) {
            player.setLevel(player.getLevel()+level);
            player.sendMessage("§a+"+level+" level");
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
    }

    public List<ItemStack> getItemStacksAward() {
        return itemStacksAward;
    }

    public int getLevel() {
        return level;
    }
}
