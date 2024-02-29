package net.lyflow.skyblock.challenge.mod.block;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PlaceBlockChallenge extends SubChallenge<BlockPlaceEvent> {

    public PlaceBlockChallenge(List<Integer> counterList, List<List<String>> elementsCounter) {
        super(Type.PLACE_BLOCK, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(BlockPlaceEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final String material = event.getBlockPlaced().getType().getKey().toString();
        if (!isValidElement(material)) return;
        incrementCounter(player, 1, material);
    }

    public static class ListenerEvent implements Listener {

        private final List<PlaceBlockChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<PlaceBlockChallenge>) challengeManager.getSubChallengesByType(Type.PLACE_BLOCK));
        }

        @EventHandler(ignoreCancelled = true)
        public void onBlockPlace(BlockPlaceEvent event) {
            challenges.forEach(placeBlockChallenge -> placeBlockChallenge.onEventTriggered(event.getPlayer(), event));
        }
    }

}
