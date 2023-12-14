package net.lyflow.skyblock.utils;

import net.lyflow.skyblock.utils.builder.StringBuilderSeparated;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class LocationUtils {

    public static Location getLocationFromString(String locationString) {
        final String[] pars = locationString.split(":");
        return new Location(Bukkit.getWorld(pars[0]), Double.parseDouble(pars[1]), Double.parseDouble(pars[2]), Double.parseDouble(pars[3]),
                Float.parseFloat(pars[4]), Float.parseFloat(pars[5]));
    }

    @Nullable
    public static String getStringFromLocation(Location location) {
        if (location.getWorld() == null) throw new RuntimeException("The world of location doesn't exist");
        return new StringBuilderSeparated(new StringBuilder(location.getWorld().getName()), ":").append(location.getX())
                .append(location.getY()).append(location.getZ()).append(location.getYaw()).append(location.getPitch()).toString();
    }

    public static String getStringFromPosition(String worldName, double x, double y, double z, float yaw, float pitch) {
        return new StringBuilderSeparated(new StringBuilder(worldName), ":").append(x)
                .append(y).append(z).append(yaw).append(pitch).toString();
    }

}
