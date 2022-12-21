package net.lyflow.skyblock.manager;

import net.lyflow.skyblock.SkyBlock;

import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.type.ReproduceAnimal;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class ChallengeManager {

    private final List<Challenge<? extends Event>> challenges = new ArrayList<>();
    private final SkyBlock skyBlock;

    public ChallengeManager(SkyBlock skyBlock) {
        this.skyBlock = skyBlock;
        registerEvent(skyBlock.getServer().getPluginManager());
    }

    private void registerEvent(PluginManager pluginManager) {
        addChallenge(new ReproduceAnimal(skyBlock, 1, Challenge.Difficulty.EASY, Challenge.Status.CURRENT), pluginManager);
        addChallenge(new ReproduceAnimal(skyBlock, 2, Challenge.Difficulty.NORMAL, Challenge.Status.LOCKED), pluginManager);
        addChallenge(new ReproduceAnimal(skyBlock, 3, Challenge.Difficulty.MEDIUM, Challenge.Status.LOCKED), pluginManager);
    }

    private void addChallenge(Challenge<? extends Event> challenge, PluginManager pluginManager) {
        pluginManager.registerEvents(challenge, skyBlock);
        challenges.add(challenge);
    }


}
