package net.lyflow.skyblock.challenge.mod.block;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class RemoveBlockChallenge extends SubChallenge<BlockBreakEvent> {

    public RemoveBlockChallenge(List<Integer> counterList, List<List<String>> elementsCounter) {
        super(Type.REMOVE_BLOCK, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(BlockBreakEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final String material = event.getBlock().getType().getKey().toString();
        if (!isValidElement(material)) return;
        incrementCounter(player, 1, material);
    }

    public static class ListenerEvent implements Listener {

        private final List<RemoveBlockChallenge> subChallenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.subChallenges = Collections.unmodifiableList((List<RemoveBlockChallenge>) challengeManager.getSubChallengesByType(Type.REMOVE_BLOCK));
        }

        @EventHandler(ignoreCancelled = true)
        public void onBlockRemove(BlockBreakEvent event) {
            subChallenges.forEach(subChallenge -> subChallenge.onEventTriggered(event.getPlayer(), event));
        }

    }

}
