package net.quillcraft.skyblock.challenge.type;

import net.quillcraft.skyblock.SkyBlock;
import net.quillcraft.skyblock.challenge.Challenge;
import net.quillcraft.skyblock.challenge.Reward;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import java.util.List;

public abstract class EntityChallenge<T extends Event> extends Challenge<T> {

    public EntityChallenge(SkyBlock skyblock, int id, Difficulty difficulty, Type type, List<Integer> linkedChallengeID, List<Integer> counterList, List<List<EntityType>> elementsCounter, Reward reward, int slot, Material material, String name, String... description) {
        super(skyblock, id, difficulty, type, linkedChallengeID, counterList, elementsCounter.stream().map(entityTypes -> entityTypes.stream().map(EntityType::name).toList()).toList(), reward, slot, material, name, description);
    }

}
