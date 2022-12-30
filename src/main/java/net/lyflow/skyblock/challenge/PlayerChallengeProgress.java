package net.lyflow.skyblock.challenge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

public class PlayerChallengeProgress {

    private final static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();

    private final HashMap<List<String>, Integer> playerCounter;
    private ChallengeStatus challengeStatus;

    public PlayerChallengeProgress(HashMap<List<String>, Integer> playerCounter, ChallengeStatus challengeStatus) {
        this.playerCounter = playerCounter;
        this.challengeStatus = challengeStatus;
    }

    public HashMap<List<String>, Integer> getPlayerCounter() {
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

    public static PlayerChallengeProgress deserialize(String data) {
        return gson.fromJson(data, new TypeToken<PlayerChallengeProgress>(){}.getType());
    }
}
