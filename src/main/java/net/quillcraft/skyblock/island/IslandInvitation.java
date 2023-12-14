package net.quillcraft.skyblock.island;

import java.util.UUID;

public record IslandInvitation(UUID playerUUID, int islandID, String targetPlayerName) {
}
