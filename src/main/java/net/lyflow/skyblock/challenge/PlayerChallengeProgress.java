package net.lyflow.skyblock.challenge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class PlayerChallengeProgress {

    private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();

    private final Map<List<String>, Integer> playerCounter;
    private ChallengeStatus challengeStatus;

    public PlayerChallengeProgress(Map<List<String>, Integer> playerCounter, ChallengeStatus challengeStatus) {
        this.playerCounter = playerCounter;
        this.challengeStatus = challengeStatus;
    }

    public static PlayerChallengeProgress deserialize(String data) {
        return gson.fromJson(data, new TypeToken<PlayerChallengeProgress>() {
        }.getType());
    }

    public Map<List<String>, Integer> getPlayerCounter() {
        return playerCounter;
    }

    public ChallengeStatus getStatus() {
        return challengeStatus;
    }

    public void setStatus(ChallengeStatus challengeStatus) {
        this.challengeStatus = challengeStatus;
    }

    public String serialize() {
        return gson.toJson(this);
    }
}
