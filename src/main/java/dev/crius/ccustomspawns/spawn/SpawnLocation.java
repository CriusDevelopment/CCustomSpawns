package dev.crius.ccustomspawns.spawn;

import org.bukkit.Location;

import java.util.List;

public class SpawnLocation {

    private final String name;
    private Location location;
    private final List<String> actions;

    public SpawnLocation(String name, Location location, List<String> actions) {
        this.name = name;
        this.location = location;
        this.actions = actions;
    }

    public List<String> getActions() {
        return actions;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }
}
