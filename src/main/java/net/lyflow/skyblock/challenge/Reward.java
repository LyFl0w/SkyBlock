package net.lyflow.skyblock.challenge;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.database.Database;
import net.lyflow.skyblock.database.request.account.AccountRequest;
import net.lyflow.skyblock.utils.StringUtils;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Reward {

    private final List<ItemStack> itemStacksAward;
    private final int level;
    private final float money;

    public Reward(List<ItemStack> itemStacksAward, int level, float money) {
        this.itemStacksAward = itemStacksAward;
        this.level = level;
        this.money = money;
    }

    public Reward(List<ItemStack> itemStacksAward) {
        this(itemStacksAward, 0, 0);
    }

    public Reward(float money) {
        this(Collections.emptyList(), 0, money);
    }

    public Reward(int level) {
        this(Collections.emptyList(), level, 0);
    }

    public Reward() {
        this(Collections.emptyList(), 0, 0);
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

        if(money > 0) {
            final Database database = SkyBlock.getInstance().getDatabase();
            try {
                final AccountRequest accountRequest = new AccountRequest(database, false);
                accountRequest.setMoney(player.getUniqueId(), accountRequest.getMoney(player.getUniqueId())+money);

                player.sendMessage("§a+"+money+" level");

                database.closeConnection();
            } catch(SQLException e) {
                throw new RuntimeException(e);
            }
        }

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 1, 1);
    }

    public List<ItemStack> getItemStacksAward() {
        return itemStacksAward;
    }

    public int getLevel() {
        return level;
    }

    public float getMoney() {
        return money;
    }

    public boolean isEmpty() {
        return (itemStacksAward.isEmpty() && level == 0 && money == 0);
    }
}
