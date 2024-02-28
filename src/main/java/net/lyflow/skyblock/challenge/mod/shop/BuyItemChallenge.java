package net.lyflow.skyblock.challenge.mod.shop;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.type.ShopChallenge;
import net.lyflow.skyblock.event.itemshop.PlayerBuyItemEvent;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.shop.ItemShop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BuyItemChallenge extends ShopChallenge<PlayerBuyItemEvent> {

    public BuyItemChallenge(List<Integer> counterList, List<List<ItemShop>> elementsCounter) {
        super(Type.BUY_ITEM, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(PlayerBuyItemEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final ItemShop itemShop = event.getItemShop();
        if (!isValidElement(itemShop)) return;
        incrementCounter(player, event.getAmount(), itemShop);
    }

    public static class ListenerEvent implements Listener {

        private final List<BuyItemChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<BuyItemChallenge>) challengeManager.getSubChallengesByType(Type.BUY_ITEM));
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerBuyItem(PlayerBuyItemEvent event) {
            challenges.forEach(buyItemChallenge -> buyItemChallenge.onEventTriggered(event.getPlayer(), event));
        }

    }

}
