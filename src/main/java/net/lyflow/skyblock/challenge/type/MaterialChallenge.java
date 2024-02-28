package net.lyflow.skyblock.challenge.type;

import net.lyflow.skyblock.challenge.SubChallenge;
import org.bukkit.Material;
import org.bukkit.event.Event;

import java.util.List;

public abstract class MaterialChallenge<T extends Event> extends SubChallenge<T> {

    protected MaterialChallenge(Type type, List<Integer> counterList, List<List<Material>> elementsCounter) {
        super(type, counterList, elementsCounter.stream().map(entityTypes -> entityTypes.stream().map(Material::name).toList()).toList());
    }

}
