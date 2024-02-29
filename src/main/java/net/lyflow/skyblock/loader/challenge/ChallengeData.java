package net.lyflow.skyblock.loader.challenge;

import net.lyflow.skyblock.SkyBlock;
import net.lyflow.skyblock.challenge.Challenge;
import net.lyflow.skyblock.challenge.SubChallenge;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ChallengeData {

    private final int id;
    private final int slot;
    private final String material;
    private final String name;
    private final List<String> description;
    private final String difficulty;
    private final List<Integer> lockedChallengesID;
    private final RewardData reward;
    private final List<SubChallengeData> subChallenge;

    public ChallengeData(int id, int slot, String material, String name, List<String> description, String difficulty, List<Integer> lockedChallengesID, RewardData reward, List<SubChallengeData> subChallenge) {
        this.id = id;
        this.slot = slot;
        this.material = material;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.lockedChallengesID = lockedChallengesID;
        this.reward = reward;
        this.subChallenge = subChallenge;
    }

    public Challenge toChallenge(SkyBlock skyBlock) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final List<SubChallenge<?>> subChallenges = new ArrayList<>();
        for (SubChallengeData subChallengeData : subChallenge) {
            subChallenges.add(subChallengeData.toSubChallenge());
        }
        return new Challenge(skyBlock, id, Challenge.Difficulty.getDifficultyByName(difficulty),
                subChallenges, (lockedChallengesID == null) ? Collections.emptyList() : lockedChallengesID,
                reward.toReward(), slot, Registry.MATERIAL.get(Objects.requireNonNull(NamespacedKey.fromString(material))),
                name, (description == null) ? new String[]{} : description.toArray(new String[0]));
    }

}
