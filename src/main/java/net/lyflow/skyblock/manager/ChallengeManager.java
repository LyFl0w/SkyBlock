package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.Reward;
import net.lyflow.skyblock.challenge.mod.CraftItemChallenge;
import net.lyflow.skyblock.challenge.mod.block.PlaceBlockChallenge;
import net.lyflow.skyblock.challenge.mod.block.RemoveBlockChallenge;
import net.lyflow.skyblock.challenge.mod.shop.BuyItemChallenge;
import net.lyflow.skyblock.challenge.mod.entity.KillEntityChallenge;
import net.lyflow.skyblock.challenge.mod.entity.ReproduceAnimalChallenge;

import net.lyflow.skyblock.challenge.mod.shop.SellItemChallenge;
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
                        new Reward(List.of(new ItemStack(Material.IRON_INGOT)), 5), 1, Material.WHEAT, "Started from the bottom")
                );
    }

    private void registerChallengesEvent(SkyBlock skyblock, PluginManager pluginManager) {
        pluginManager.registerEvents(new ReproduceAnimalChallenge.ListenerEvent(this), skyblock);
        pluginManager.registerEvents(new KillEntityChallenge.ListenerEvent(this), skyblock);

        pluginManager.registerEvents(new BuyItemChallenge.ListenerEvent(this), skyblock);
        pluginManager.registerEvents(new SellItemChallenge.ListenerEvent(this), skyblock);

        pluginManager.registerEvents(new PlaceBlockChallenge.ListenerEvent(this), skyblock);
        pluginManager.registerEvents(new RemoveBlockChallenge.ListenerEvent(this), skyblock);

        pluginManager.registerEvents(new CraftItemChallenge.ListenerEvent(this), skyblock);

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
