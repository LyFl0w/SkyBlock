package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.Reward;
import net.lyflow.skyblock.challenge.mod.ReproduceAnimalChallenge;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChallengeManager {

    private final List<Challenge<? extends Event, ?>> registeredChallenges = new ArrayList<>();

    public ChallengeManager(SkyBlock skyBlock) {
        createChallenges(skyBlock);
        registerChallengesEvent(skyBlock, skyBlock.getServer().getPluginManager());
    }

    private void createChallenges(SkyBlock skyBlock) {
        new ReproduceAnimalChallenge(skyBlock, 0, Challenge.Difficulty.EASY, Arrays.asList(1, 2),
                List.of(Arrays.asList(EntityType.COW, EntityType.SHEEP)),
                new Reward(Arrays.asList(new ItemStack(Material.APPLE, 2), new ItemStack(Material.DIAMOND))));

        new ReproduceAnimalChallenge(skyBlock, 1, Challenge.Difficulty.EASY, Arrays.asList(5, 2),
                Arrays.asList(Arrays.asList(EntityType.COW, EntityType.SHEEP), List.of(EntityType.PIG)),
                new Reward(List.of(new ItemStack(Material.IRON_INGOT, 5)), 5));
    }

    private void registerChallengesEvent(SkyBlock skyBlock, PluginManager pluginManager) {
        pluginManager.registerEvents(new ReproduceAnimalChallenge.ListenerEvent(this), skyBlock);
    }

    public List<Challenge<? extends Event, ?>> getRegisteredChallenges() {
        return registeredChallenges;
    }

    public boolean challengeExist(int id) {
        return registeredChallenges.stream().parallel().anyMatch(challenge -> challenge.getId() == id);
    }

    @Nullable
    public Challenge<? extends Event, ?> getChallengeByID(int id) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getId() == id).findFirst().orElse(null);
    }

    public List<? extends Challenge<? extends Event, ?>> getChallengesByType(Challenge.Type type) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getType() == type).toList();
    }

}
