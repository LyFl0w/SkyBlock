package net.lyflow.skyblock.challenge.type;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.Reward;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import java.util.List;

public abstract class EntityChallenge<T extends Event> extends Challenge<T, EntityType> {

    public EntityChallenge(SkyBlock skyblock, int id, Difficulty difficulty, Type type, List<Integer> counterList, List<List<EntityType>> elementsCounter, Reward reward, Material material, String name, String... description) {
        super(skyblock, id, difficulty, type, counterList, elementsCounter, reward, material, name, description);
    }

}
