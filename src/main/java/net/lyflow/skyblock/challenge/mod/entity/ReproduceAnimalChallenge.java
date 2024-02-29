package net.lyflow.skyblock.challenge.mod.entity;

import net.lyflow.skyblock.challenge.PlayerChallenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.manager.ChallengeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ReproduceAnimalChallenge extends SubChallenge<EntityBreedEvent> {

    public ReproduceAnimalChallenge(List<Integer> counterList, List<List<String>> elementsCounter) {
        super(Type.REPRODUCE_ANIMAL, counterList, elementsCounter);
    }

    @Override
    protected void onEvent(EntityBreedEvent event, Player player, PlayerChallenge playerChallengeProgress) throws SQLException {
        final String entityType = event.getEntityType().getKey().toString();
        if (!isValidElement(entityType)) return;
        incrementCounter(player, 1, entityType);
    }

    public static class ListenerEvent implements Listener {

        private final List<ReproduceAnimalChallenge> challenges;

        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<ReproduceAnimalChallenge>) challengeManager.getSubChallengesByType(Type.REPRODUCE_ANIMAL));
        }

        @EventHandler(ignoreCancelled = true)
        public void onEntityBreed(EntityBreedEvent event) {
            if (!(event.getBreeder() instanceof final Player player)) return;
            challenges.forEach(reproduceAnimalChallenge -> reproduceAnimalChallenge.onEventTriggered(player, event));
        }

    }

}
