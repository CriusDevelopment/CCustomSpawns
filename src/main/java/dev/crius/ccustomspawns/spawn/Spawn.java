package dev.crius.ccustomspawns.spawn;

import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class Spawn {

    private final Random random = new SecureRandom();
    private final String name;
    private final List<SpawnLocation> locations;
    private final List<String> ips;
    private String permission;

    public Spawn(String name, List<SpawnLocation> locations, List<String> ips, @Nullable String permission) {
        this.name = name;
        this.locations = locations;
        this.ips = ips;
        this.permission = permission;
    }

    public List<SpawnLocation> getLocations() {
        return locations;
    }

    public List<String> getIps() {
        return ips;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public SpawnLocation randomLocation() {
        return locations.get(random.nextInt(locations.size()));
    }

    public String getName() {
        return name;
    }
}
