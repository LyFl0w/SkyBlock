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

    public ChallengeManager(SkyBlock skyblock) {
        createChallenges(skyblock);
        registerChallengesEvent(skyblock, skyblock.getServer().getPluginManager());
    }

    private void createChallenges(SkyBlock skyblock) {
        addNewChallenges(
                new ReproduceAnimalChallenge(skyblock, 1, Challenge.Difficulty.EASY, Arrays.asList(5, 2),
                        Arrays.asList(Arrays.asList(EntityType.COW, EntityType.SHEEP), List.of(EntityType.PIG)),
                        new Reward(List.of(new ItemStack(Material.IRON_INGOT, 5)), 5))
                );
    }

    private void registerChallengesEvent(SkyBlock skyblock, PluginManager pluginManager) {
        pluginManager.registerEvents(new ReproduceAnimalChallenge.ListenerEvent(this), skyblock);
    }

    public List<Challenge<? extends Event, ?>> getRegisteredChallenges() {
        return registeredChallenges;
    }

    public boolean challengeExist(int id) {
        return registeredChallenges.stream().parallel().anyMatch(challenge -> challenge.getID() == id);
    }

    @Nullable
    public Challenge<? extends Event, ?> getChallengeByID(int id) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getID() == id).findFirst().orElse(null);
    }

    public List<? extends Challenge<? extends Event, ?>> getChallengesByType(Challenge.Type type) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getType() == type).toList();
    }

    @SafeVarargs
    public final void addNewChallenges(Challenge<? extends Event, ?>... challenges) {
        Arrays.stream(challenges).forEach(challenge ->  {
            final int id = challenge.getID();
            if(challengeExist(id)) {
                final Challenge<?, ?> otherChallenge = getChallengeByID(id);
                throw new RuntimeException((challenge.equals(otherChallenge)) ? "Their is a duplication of Challenge with id "+id : "The Challenge "+challenge.getName()+" can't be initialized because his id is already use by the Challenge "+otherChallenge.getName());
            }
            getRegisteredChallenges().add(challenge);
        });

    }

}
