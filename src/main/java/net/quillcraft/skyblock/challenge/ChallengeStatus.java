package net.quillcraft.skyblock.challenge;

public enum ChallengeStatus {

    LOCKED, IN_PROGRESS, SUCCESSFUL, REWARD_RECOVERED;

    ChallengeStatus() {
    }

    public boolean isFinish() {
        return this == SUCCESSFUL || this == REWARD_RECOVERED;
    }

}