package net.lyflow.skyblock.challenge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.event.Event;

import java.util.List;
import java.util.UUID;

public class PlayerChallenge {

    private final static Gson gson = new GsonBuilder().serializeNulls().create();

    private final UUID uuid;
    private final List<Challenge<? extends Event>> challenges;

    public PlayerChallenge(UUID uuid, List<Challenge<? extends Event>> challenges) {
        this.uuid = uuid;
        this.challenges = challenges;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<Challenge<? extends Event>> getChallenges() {
        return challenges;
    }

    public String serializeChallenges() {
        return gson.toJson(challenges);
    }

    public static PlayerChallenge deserialize(UUID uuid, String challengesSerialize) {
        return new PlayerChallenge(uuid, gson.fromJson(challengesSerialize, new TypeToken<List<Challenge<? extends Event>>>(){}.getType()));
    }

}
