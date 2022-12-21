package net.lyflow.skyblock.challenge.type;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.database.request.challenge.ChallengeRequest;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityBreedEvent;

public class ReproduceAnimal extends Challenge<EntityBreedEvent> {

    public ReproduceAnimal(SkyBlock skyBlock, int id, Difficulty difficulty, Status defaultStatus) {
        super(skyBlock, id, difficulty, Type.REPRODUCE_ANIMAL, defaultStatus);
    }

    @Override @EventHandler
    public void onEvent(EntityBreedEvent event) {
        if(!(event.getBreeder() instanceof final Player player)) return;
        final ChallengeRequest challengeRequest = new ChallengeRequest(skyBlock.getDatabase(), false);

    }

}
