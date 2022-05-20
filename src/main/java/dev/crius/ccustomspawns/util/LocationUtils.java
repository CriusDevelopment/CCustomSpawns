package dev.crius.ccustomspawns.util;

import dev.crius.ccustomspawns.spawn.SpawnLocation;
import io.papermc.lib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static Location getLocation(String location) {
        if (location == null || location.isEmpty())
            return null;

        String[] sections = location.split(",");

        double x = Double.parseDouble(sections[1]);
        double y = Double.parseDouble(sections[2]);
        double z = Double.parseDouble(sections[3]);
        float yaw = sections.length > 5 ? Float.parseFloat(sections[4]) : 0;
        float pitch = sections.length > 4 ? Float.parseFloat(sections[5]) : 0;

        return new Location(Bukkit.getWorld(sections[0]), x, y, z, yaw, pitch);
    }

    public static String getLocation(Location location) {
        if (location == null || location.getWorld() == null) return "Unknown location";

        return location.getWorld().getName() + "," + location.getX() +
                "," + location.getY() +
                "," + location.getZ() +
                "," + location.getYaw() +
                "," + location.getPitch();
    }

    public static void teleport(Player player, Location location) {
        PaperLib.teleportAsync(player, location);
    }

    public static void teleport(Player player, SpawnLocation spawnLocation) {
        teleport(player, spawnLocation.getLocation());
    }

}
