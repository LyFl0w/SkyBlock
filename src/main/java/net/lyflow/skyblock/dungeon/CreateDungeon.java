package net.lyflow.skyblock.dungeon;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.structure.Structure;

import java.util.Objects;

public class CreateDungeon {

    private final int index;
    private final String dungeonType;
    private final Structure structure;

    private final String name;

    public CreateDungeon(int index) {
        this.index = index;
        this.dungeonType = "minecraft:trial_chambers";
        this.structure = Registry.STRUCTURE.get(Objects.requireNonNull(NamespacedKey.fromString(dungeonType)));

        String[] splitType = dungeonType.split(":");
        this.name = "dungeon/" + splitType[0] + "/" + splitType[1] + "/" +index;
    }

    public void createWorld() {
        System.out.println("Create world ! ( name : " + name + " )");

        final WorldCreator worldCreator = new WorldCreator(name);
        worldCreator.type(WorldType.FLAT);
        worldCreator.generatorSettings("{\"layers\": [{\"block\": \"bedrock\", \"height\": 2}, {\"block\": \"stone\", \"height\": 150}], \"biome\":\"plains\"}");
        worldCreator.createWorld();
    }

    public int getIndex() {
        return index;
    }
}
