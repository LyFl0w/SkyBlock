package net.lyflow.skyblock.challenge.mod.entity;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.PlayerChallengeProgress;
import net.lyflow.skyblock.challenge.Reward;
import net.lyflow.skyblock.challenge.type.EntityChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class KillEntityChallenge extends EntityChallenge<EntityDeathEvent> {

    public KillEntityChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> lockedChallengeID, List<Integer> counterList, List<List<EntityType>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        super(skyblock, id, difficulty, Type.KILL_ENTITY, lockedChallengeID, counterList, elementsCounter, reward, slot, material, name, description);
    }

    public KillEntityChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> counterList, List<List<EntityType>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        this(skyblock, id, difficulty, Collections.emptyList(), counterList, elementsCounter, reward, slot, material, name, description);
    }

    @Override
    protected void onEvent(EntityDeathEvent event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException {
        final EntityType entityType = event.getEntityType();
        if (challengeProgress.isNotValidElement(entityType)) return;
        challengeProgress.incrementCounter(player, 1, entityType);
    }

    public static class ListenerEvent implements Listener {

        private final List<KillEntityChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<KillEntityChallenge>) challengeManager.getChallengesByType(Type.KILL_ENTITY));
        }

        @EventHandler(ignoreCancelled = true)
        public void onEntityDeathEvent(EntityDeathEvent event) {
            final Player player = event.getEntity().getKiller();
            if (player == null) return;
            challenges.forEach(killEntityChallenge -> killEntityChallenge.onEventTriggered(player, event));
        }
    }

}
