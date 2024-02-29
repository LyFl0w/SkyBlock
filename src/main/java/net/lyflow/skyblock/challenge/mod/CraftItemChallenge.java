package net.lyflow.skyblock.challenge.mod;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import net.lyflow.skyblock.utils.InventoryUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class CraftItemChallenge extends SubChallenge<CraftItemEvent> {

    public CraftItemChallenge(List<Integer> linkedChallengeID, List<List<String>> elementsCounter) {
        super(Type.CRAFT_ITEM, linkedChallengeID, elementsCounter);
    }

    @Override
    protected void onEvent(CraftItemEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final ItemStack itemStack = event.getRecipe().getResult();
        final String material = itemStack.getType().getKey().toString();
        if (!isValidElement(material)) return;
        incrementCounter(player, (event.isShiftClick()
                ? InventoryUtils.getCraftedItemAmount(event.getInventory())
                : itemStack.getAmount()), material);
    }

    public static class ListenerEvent implements Listener {

        private final List<CraftItemChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<CraftItemChallenge>) challengeManager.getSubChallengesByType(Type.CRAFT_ITEM));
        }

        @EventHandler(ignoreCancelled = true)
        public void onCraftItem(CraftItemEvent event) {
            challenges.forEach(placeBlockChallenge -> placeBlockChallenge.onEventTriggered((Player) event.getWhoClicked(), event));
        }

    }

}
