package net.lyflow.skyblock.challenge.type;

import net.lyflow.skyblock.challenge.SubChallenge;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;

import java.util.List;

public abstract class EntityChallenge<T extends Event> extends SubChallenge<T> {

    protected EntityChallenge(Type type, List<Integer> counterList, List<List<EntityType>> elementsCounter) {
        super(type, counterList, elementsCounter.stream().map(entityTypes -> entityTypes.stream().map(EntityType::name).toList()).toList());
    }

}
