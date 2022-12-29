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

    private final List<Challenge<? extends Event>> registeredChallenges = new ArrayList<>();

    public ChallengeManager(SkyBlock skyblock) {
        createChallenges(skyblock);
        registerChallengesEvent(skyblock, skyblock.getServer().getPluginManager());
    }

    public void init() {
        registeredChallenges.stream().parallel().forEach(challenge -> challenge.getChallengeProgress().updateDefaultChallengeStatus());
    }


    private void createChallenges(SkyBlock skyblock) {
        addNewChallenges(
                new ReproduceAnimalChallenge(skyblock, 0, Challenge.Difficulty.EASY, List.of(1), Arrays.asList(1, 1), Arrays.asList(Arrays.asList(EntityType.COW, EntityType.CHICKEN), List.of(EntityType.PIG)),
                        new Reward(List.of(new ItemStack(Material.IRON_INGOT)), 5), 1, Material.WHEAT, "Started from the bottom"),

                new ReproduceAnimalChallenge(skyblock, 1, Challenge.Difficulty.EASY, List.of(4), List.of(5), List.of(List.of(EntityType.SHEEP)),
                        new Reward(List.of(new ItemStack(Material.IRON_INGOT, 5))), 2, Material.APPLE, "Still at the bottom"),

                new ReproduceAnimalChallenge(skyblock, 2, Challenge.Difficulty.EASY, List.of(4), List.of(2), List.of(List.of(EntityType.CHICKEN)),
                        new Reward(List.of(new ItemStack(Material.IRON_INGOT, 5))), 3, Material.APPLE, "Still at the bottom n°2"),


                new ReproduceAnimalChallenge(skyblock, 3, Challenge.Difficulty.NORMAL, List.of(4), List.of(5), List.of(List.of(EntityType.PIG)),
                        new Reward(List.of(new ItemStack(Material.DIAMOND, 2))), 1, Material.BONE_MEAL, "Step up bis", "don't stay stand up"),

                new ReproduceAnimalChallenge(skyblock, 4, Challenge.Difficulty.NORMAL, List.of(10), List.of(List.of(EntityType.COW)),
                        new Reward(List.of(new ItemStack(Material.DIAMOND, 2))), 2, Material.BONE_MEAL, "Step up", "stay stand up")
                );
    }

    private void registerChallengesEvent(SkyBlock skyblock, PluginManager pluginManager) {
        pluginManager.registerEvents(new ReproduceAnimalChallenge.ListenerEvent(this), skyblock);
    }

    public List<Challenge<? extends Event>> getRegisteredChallenges() {
        return registeredChallenges;
    }

    public boolean challengeExist(int id) {
        return registeredChallenges.stream().parallel().anyMatch(challenge -> challenge.getID() == id);
    }

    public boolean challengeExist(String name) {
        return registeredChallenges.stream().parallel().anyMatch(challenge -> challenge.getName().equalsIgnoreCase(name));
    }

    @Nullable
    public Challenge<? extends Event> getChallengeByID(int id) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getID() == id).findFirst().orElse(null);
    }

    @Nullable
    public Challenge<? extends Event> getChallengeByName(String name) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Nullable
    public List<? extends Challenge<? extends Event>> getChallengesByType(Challenge.Type type) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getType() == type).toList();
    }

    @Nullable
    public List<? extends Challenge<? extends Event>> getChallengesByDifficulty(Challenge.Difficulty difficulty) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getDifficulty() == difficulty).toList();
    }

    @SafeVarargs
    public final void addNewChallenges(Challenge<? extends Event>... challenges) {
        Arrays.stream(challenges).forEach(challenge ->  {
            final String name = challenge.getName();
            final int id = challenge.getID();

            if(challengeExist(id)) {
                final Challenge<?> otherChallenge = getChallengeByID(id);
                throw new RuntimeException((challenge.equals(otherChallenge)) ? "Their is a duplication of Challenge with id "+id : "The Challenge "+challenge.getName()+" can't be initialized because his id is already use by the Challenge "+otherChallenge.getName());
            } else if(challengeExist(name)) {
                final Challenge<?> otherChallenge = getChallengeByName(name);
                throw new RuntimeException((challenge.equals(otherChallenge)) ? "Their is a duplication of Challenge with name "+name : "The Challenge "+challenge.getName()+" can't be initialized because his name is already use by this Challenge ID "+otherChallenge.getID());
            }

            getRegisteredChallenges().add(challenge);
        });
    }

}
