package net.lyflow.skyblock.challenge.mod.shop;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.event.itemshop.PlayerSellItemEvent;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class SellItemChallenge extends SubChallenge<PlayerSellItemEvent> {

    public SellItemChallenge(List<Integer> counterList, List<List<String>> elementsCounter) {
        super(Type.SELL_ITEM, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(PlayerSellItemEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final String itemShop = event.getItemShop().getMaterial().getKey().toString();
        if (!isValidElement(itemShop)) return;
        incrementCounter(player, event.getAmount(), itemShop);
    }

    public static class ListenerEvent implements Listener {

        private final List<SellItemChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<SellItemChallenge>) challengeManager.getSubChallengesByType(Type.SELL_ITEM));
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerSellItem(PlayerSellItemEvent event) {
            challenges.forEach(sellItemChallenge -> sellItemChallenge.onEventTriggered(event.getPlayer(), event));
        }

    }

}
