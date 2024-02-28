package net.lyflow.skyblock.challenge.mod.block;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.type.MaterialChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class RemoveBlockChallenge extends MaterialChallenge<BlockBreakEvent> {

    public RemoveBlockChallenge(List<Integer> counterList, List<List<Material>> elementsCounter) {
        super(Type.REMOVE_BLOCK, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(BlockBreakEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final Material material = event.getBlock().getType();
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
