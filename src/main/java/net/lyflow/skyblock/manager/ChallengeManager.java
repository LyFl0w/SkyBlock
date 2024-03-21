package net.lyflow.skyblock.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import net.lyflow.skyblock.challenge.mod.CraftItemChallenge;
import net.lyflow.skyblock.challenge.mod.block.PlaceBlockChallenge;
import net.lyflow.skyblock.challenge.mod.block.RemoveBlockChallenge;
import net.lyflow.skyblock.challenge.mod.entity.KillEntityChallenge;
import net.lyflow.skyblock.challenge.mod.entity.ReproduceAnimalChallenge;
import net.lyflow.skyblock.challenge.mod.shop.BuyItemChallenge;
import net.lyflow.skyblock.challenge.mod.shop.SellItemChallenge;
import net.lyflow.skyblock.loader.challenge.ChallengeData;
import net.lyflow.skyblock.loader.challenge.RewardData;
import net.lyflow.skyblock.loader.challenge.SubChallengeData;
import net.lyflow.skyblock.loader.gson.EmptyListToNullFactory;
import net.lyflow.skyblock.loader.island.upgrade.IslandUpgradeData;
import net.lyflow.skyblock.loader.minecraft.EnchantmentData;
import net.lyflow.skyblock.loader.minecraft.ItemStackData;
import net.lyflow.skyblock.utils.ResourceUtils;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChallengeManager {

    private final List<Challenge> registeredChallenges = new ArrayList<>();

    public ChallengeManager(SkyBlock skyblock) {
        createChallenges(skyblock);
        registerChallengesEvent(skyblock, skyblock.getServer().getPluginManager());
    }

    public void init() {
        registeredChallenges.stream().parallel().forEach(challenge -> challenge.getChallengeProgressManager().updateDefaultChallengeStatus());
    }

    private void createChallenges(SkyBlock skyblock) {

        final Gson gson = new GsonBuilder()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapterFactory(EmptyListToNullFactory.INSTANCE)
                .setPrettyPrinting()
                .create();

        final String name = "data/challenge";
        final File challengeFolder = new File(skyblock.getDataFolder(), name);
        if(!challengeFolder.exists()) {
            skyblock.getLogger().info("Generate Challenge folder in plugin folder (" + name + ")");
            ResourceUtils.saveResourceFolder(name, challengeFolder, skyblock, false);
        }

        for(File configurationFile : Objects.requireNonNull(challengeFolder.listFiles())) {
            try {
                final ChallengeData challengeData = gson.fromJson(new FileReader(configurationFile), ChallengeData.class);
                addNewChallenge(challengeData.toChallenge(skyblock));
            } catch (FileNotFoundException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        /*addNewChallenges(
                new Challenge(skyblock, 1, Challenge.Difficulty.EASY,
                        List.of(new RemoveBlockChallenge(List.of(6), List.of(List.of("COBBLESTONE")))),
                        new Reward(List.of(new ItemStack(Material.BREAD, 5))),
                        2, Material.COBBLESTONE, "La voie du mineur n°1",
                        "La pierre tout comme le bois est un element essentiel"
                ),

                new Challenge(skyblock, 2, Challenge.Difficulty.EASY,
                        List.of(new CraftItemChallenge(List.of(1, 1), Arrays.asList(List.of("STONE_AXE"), List.of("STONE_PICKAXE")))),
                        new Reward(), 3, Material.SCULK_SENSOR, "Started from the bottom"
                ),

                new Challenge(skyblock, 3, Challenge.Difficulty.EASY,
                        List.of(new SellItemChallenge(List.of(64), List.of(List.of("COBBLESTONE")))),
                        new Reward(List.of(new ItemStack(Material.COW_SPAWN_EGG, 2)), 2, 0),
                        4, Material.SALMON, "Still at the bottom n°1"
                ),

                new Challenge(skyblock, 4, Challenge.Difficulty.EASY,
                        List.of(new SellItemChallenge(List.of(64), List.of(List.of("OAK_LOG")))),
                        new Reward(List.of(new ItemStack(Material.SHEEP_SPAWN_EGG, 2)), 2, 0),
                        5, Material.COOKED_COD, "Still at the bottom n°2"
                ),

                new Challenge(skyblock, 5, Challenge.Difficulty.EASY,
                        List.of(
                                new ReproduceAnimalChallenge(List.of(3), List.of(List.of("COW", "SHEEP"))),
                                new KillEntityChallenge(List.of(3), List.of(List.of("COW", "SHEEP")))
                        ),
                        new Reward(List.of(new ItemStack(Material.SHEEP_SPAWN_EGG, 64)), 128, 55),
                        6, Material.COOKED_COD, "Animalien !"
                ),

                new Challenge(skyblock, 6, Challenge.Difficulty.EASY,
                        List.of(
                                new KillEntityChallenge(List.of(5), List.of(List.of("COW", "SHEEP")))
                        ),
                        new Reward(List.of(new ItemStack(Material.SHEEP_SPAWN_EGG, 64)), 128, 55),
                        7, Material.COOKED_COD, "Animalien 2!"
                )
        );*/
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

    public List<Challenge> getRegisteredChallenges() {
        return registeredChallenges;
    }

    public boolean challengeExist(int id) {
        return registeredChallenges.stream().parallel().anyMatch(challenge -> challenge.getID() == id);
    }

    public boolean challengeExist(String name) {
        return registeredChallenges.stream().parallel().anyMatch(challenge -> challenge.getName().equalsIgnoreCase(name));
    }

    @Nullable
    public Challenge getChallengeByID(int id) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getID() == id).findFirst().orElse(null);
    }

    @Nullable
    public Challenge getChallengeByName(String name) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Nullable
    public List<Challenge> getChallengesByDifficulty(Challenge.Difficulty difficulty) {
        return registeredChallenges.stream().parallel().filter(challenge -> challenge.getDifficulty() == difficulty).toList();
    }

    public final void addNewChallenges(Challenge... challenges) {
        Arrays.stream(challenges).forEach(this::addNewChallenge);
    }

    public final void addNewChallenge(Challenge challenge) {
        final String name = challenge.getName();
        final int id = challenge.getID();

        if (challengeExist(id)) {
            final Challenge otherChallenge = getChallengeByID(id);
            throw new IllegalArgumentException((challenge.equals(otherChallenge))
                    ? "Their is a duplication of Challenge with id " + id
                    : "The Challenge " + challenge.getName() + " can't be initialized because his id is already use by the Challenge " + otherChallenge.getName());
        } else if (challengeExist(name)) {
            final Challenge otherChallenge = getChallengeByName(name);
            throw new IllegalArgumentException((challenge.equals(otherChallenge))
                    ? "Their is a duplication of Challenge with name " + name
                    : "The Challenge " + challenge.getName() + " can't be initialized because his name is already use by this Challenge ID " + otherChallenge.getID());
        }

        getRegisteredChallenges().add(challenge);
    }

    public List<? extends SubChallenge<? extends Event>> getSubChallengesByType(SubChallenge.Type type) {
        final List<SubChallenge<?>> subChallenges = new ArrayList<>();
        registeredChallenges.stream().parallel().forEach(challenge -> {
            for (SubChallenge<?> subChallenge : challenge.getChallengeProgressManager().getSubChallenges()) {
                if (subChallenge.getType() == type) {
                    subChallenges.add(subChallenge);
                    break;
                }
            }
        });

        return subChallenges;
    }
}