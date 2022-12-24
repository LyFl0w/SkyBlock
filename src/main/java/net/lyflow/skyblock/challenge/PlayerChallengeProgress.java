package net.lyflow.skyblock.challenge;

import java.util.HashMap;
import java.util.List;

public class PlayerChallengeProgress<T> {

    private final HashMap<List<T>, Integer> playerCounter;
    private ChallengeStatus challengeStatus;

    public PlayerChallengeProgress(HashMap<List<T>, Integer> playerCounter, ChallengeStatus challengeStatus) {
        this.playerCounter = playerCounter;
        this.challengeStatus = challengeStatus;
    }

    public HashMap<List<T>, Integer> getPlayerCounter() {
        return playerCounter;
    }

    public ChallengeStatus getStatus() {
        return challengeStatus;
    }

    public void setStatus(ChallengeStatus challengeStatus) {
        this.challengeStatus = challengeStatus;
    }
}
