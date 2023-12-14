package net.quillcraft.skyblock.challenge.mod.shop;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.challenge.PlayerChallengeProgress;
import net.quillcraft.skyblock.challenge.Reward;
import net.quillcraft.skyblock.challenge.type.ShopChallenge;
import net.quillcraft.skyblock.event.itemshop.PlayerBuyItemEvent;
import net.quillcraft.skyblock.manager.ChallengeManager;
import net.quillcraft.skyblock.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class BuyItemChallenge extends ShopChallenge<PlayerBuyItemEvent> {

    public BuyItemChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> lockedChallengeID, List<Integer> counterList, List<List<ItemShop>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        super(skyblock, id, difficulty, Type.BUY_ITEM, lockedChallengeID, counterList, elementsCounter, reward, slot, material, name, description);
    }

    public BuyItemChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> counterList, List<List<ItemShop>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        this(skyblock, id, difficulty, Collections.emptyList(), counterList, elementsCounter, reward, slot, material, name, description);
    }

    @Override
    protected void onEvent(PlayerBuyItemEvent event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException {
        final ItemShop itemShop = event.getItemShop();
        if (!challengeProgress.isValidElement(itemShop)) return;
        challengeProgress.incrementCounter(player, event.getAmount(), itemShop);
    }

    public static class ListenerEvent implements Listener {

        private final List<BuyItemChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<BuyItemChallenge>) challengeManager.getChallengesByType(Type.BUY_ITEM));
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerBuyItem(PlayerBuyItemEvent event) {
            challenges.stream().parallel().forEach(buyItemChallenge -> buyItemChallenge.onEventTriggered(event.getPlayer(), event));
        }

    }

}
