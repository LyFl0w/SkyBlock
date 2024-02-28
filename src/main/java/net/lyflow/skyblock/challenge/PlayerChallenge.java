package net.lyflow.skyblock.challenge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

public class PlayerChallenge {

    private static final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

    private final Map<SubChallenge.Type, Progress> playerCounters;
    private ChallengeStatus challengeStatus;

    public PlayerChallenge(Map<SubChallenge.Type, Progress> playerCounters, ChallengeStatus challengeStatus) {
        this.playerCounters = playerCounters;
        this.challengeStatus = challengeStatus;
    }

    public Map<SubChallenge.Type, Progress> getPlayerCounters() {
        return playerCounters;
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

    public static PlayerChallenge deserialize(String data) {
        return gson.fromJson(data, new TypeToken<PlayerChallenge>() {
        }.getType());
    }

    public static class Progress {

        private final Map<List<String>, Integer> counter;
        private SubChallengeStatus subChallengeStatus;

        public Progress(Map<List<String>, Integer> counter, SubChallengeStatus subChallengeStatus) {
            this.counter = counter;
            this.subChallengeStatus = subChallengeStatus;
        }

        public Map<List<String>, Integer> getCounter() {
            return counter;
        }

        public SubChallengeStatus getStatus() {
            return subChallengeStatus;
        }

        public void setStatus(SubChallengeStatus subChallengeStatus) {
            this.subChallengeStatus = subChallengeStatus;
        }
    }
}
