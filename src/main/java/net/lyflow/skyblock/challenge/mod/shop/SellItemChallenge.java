package net.lyflow.skyblock.challenge.mod.shop;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.PlayerChallengeProgress;
import net.lyflow.skyblock.challenge.Reward;
import net.lyflow.skyblock.challenge.type.ShopChallenge;
import net.lyflow.skyblock.event.itemshop.PlayerSellItemEvent;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.shop.ItemShop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SellItemChallenge extends ShopChallenge<PlayerSellItemEvent> {

    public SellItemChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> lockedChallengeID, List<Integer> counterList, List<List<ItemShop>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        super(skyblock, id, difficulty, Type.SELL_ITEM, lockedChallengeID, counterList, elementsCounter, reward, slot, material, name, description);
    }

    public SellItemChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> counterList, List<List<ItemShop>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        this(skyblock, id, difficulty, Collections.emptyList(), counterList, elementsCounter, reward, slot, material, name, description);
    }

    @Override
    protected void onEvent(PlayerSellItemEvent event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException {
        final ItemShop itemShop = event.getItemShop();
        if(!challengeProgress.isValidElement(itemShop)) return;
        challengeProgress.incrementCounter(player, event.getAmount(), itemShop);
    }

    public static class ListenerEvent implements Listener {

        private final List<SellItemChallenge> challenges;
        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<SellItemChallenge>) challengeManager.getChallengesByType(Type.SELL_ITEM));
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerSellItem(PlayerSellItemEvent event) {
            challenges.stream().parallel().forEach(sellItemChallenge -> sellItemChallenge.onEventTriggered(event.getPlayer(), event));
        }

    }

}
