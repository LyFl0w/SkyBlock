package net.lyflow.skyblock.challenge;

public enum SubChallengeStatus {

    IN_PROGRESS, SUCCESSFUL;

    SubChallengeStatus() {
    }

    public boolean isFinish() {
        return this == SUCCESSFUL;
    }

}