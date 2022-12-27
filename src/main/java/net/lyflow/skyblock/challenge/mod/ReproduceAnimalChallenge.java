package net.lyflow.skyblock.challenge.mod;

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
import org.bukkit.event.entity.EntityBreedEvent;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ReproduceAnimalChallenge extends EntityChallenge<EntityBreedEvent> {

    public ReproduceAnimalChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> counterList, List<List<EntityType>> elementsCounter, Reward reward, Material material, String name, String... description) {
        super(skyblock, id, difficulty, Type.REPRODUCE_ANIMAL, counterList, elementsCounter, reward, material, name, description);
    }

    public ReproduceAnimalChallenge(SkyBlock skyblock, int id, Difficulty difficulty, List<Integer> counterList, List<List<EntityType>> elementsCounter, Reward reward) {
        this(skyblock, id, difficulty, counterList, elementsCounter, reward, Material.WHEAT, "Defi de fou sur la reproduction d'animaux", "generic descrition", "oui il y a une faute");
    }

    @Override
    protected void onEvent(EntityBreedEvent event, Player player, PlayerChallengeProgress playerChallengeProgress) throws SQLException {
        final EntityType entityType = event.getEntityType();
        if(!challengeProgress.isValidElement(entityType)) return;
        challengeProgress.incrementCounter(player, 1, entityType);
    }

    public static class ListenerEvent implements Listener {

        private final List<ReproduceAnimalChallenge> challenges;
        public ListenerEvent(ChallengeManager challengeManager) {
            this.challenges = Collections.unmodifiableList((List<ReproduceAnimalChallenge>) challengeManager.getChallengesByType(Type.REPRODUCE_ANIMAL));
        }

        @EventHandler
        public void onEntityBreed(EntityBreedEvent event) {
            if(!(event.getBreeder() instanceof final Player player)) return;
            challenges.stream().parallel().forEach(reproduceAnimalChallenge -> reproduceAnimalChallenge.onEventTriggered(player, event));
        }

    }

}
