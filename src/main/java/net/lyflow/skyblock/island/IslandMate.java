package net.lyflow.skyblock.island;

import org.bukkit.OfflinePlayer;

public record IslandMate(OfflinePlayer player, MateStatus status) {

    public int isOnline() {
        return (player.isOnline()) ? 1 : 0;
    }

    public String getStatus() {
        final String isOwner = (status == MateStatus.OWNER) ? "Owner/" : "";
        return (player.isOnline()) ? "["+isOwner+"Online]" : "["+isOwner+"Offline]";
    }

}
