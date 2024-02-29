package net.lyflow.skyblock.challenge.mod.entity;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class KillEntityChallenge extends SubChallenge<EntityDeathEvent> {

    public KillEntityChallenge(List<Integer> counterList, List<List<String>> elementsCounter) {
        super(Type.KILL_ENTITY, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(EntityDeathEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final String entityType = event.getEntityType().getKey().toString();
        if (!isValidElement(entityType)) return;
        incrementCounter(player, 1, entityType);
    }

    public static class ListenerEvent implements Listener {

        private final List<KillEntityChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<KillEntityChallenge>) challengeManager.getSubChallengesByType(Type.KILL_ENTITY));
        }

        @EventHandler(ignoreCancelled = true)
        public void onEntityDeathEvent(EntityDeathEvent event) {
            final Player player = event.getEntity().getKiller();
            if (player == null) return;
            challenges.forEach(killEntityChallenge -> killEntityChallenge.onEventTriggered(player, event));
        }
    }

}
