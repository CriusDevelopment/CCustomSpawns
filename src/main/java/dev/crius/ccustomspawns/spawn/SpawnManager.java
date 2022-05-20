package dev.crius.ccustomspawns.spawn;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.util.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class SpawnManager {

    private final Map<String, Spawn> spawnMap = new HashMap<>();
    private final CCustomSpawnsPlugin plugin;

    public SpawnManager(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        spawnMap.clear();

        ConfigurationSection section = plugin.getData().getConfigurationSection("Spawns");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            String permission = section.getString(key + ".permission");
            List<String> ips = section.getStringList(key + ".ips");
            List<SpawnLocation> spawnLocations = new ArrayList<>();

            Spawn spawn = new Spawn(key, spawnLocations, ips, permission);

            ConfigurationSection lSection = section.getConfigurationSection(key + ".locations");
            if (lSection != null) {
                for (String l : lSection.getKeys(false)) {
                    String location = section.getString(key + ".locations." + l + ".location");
                    List<String> actions = section.getStringList(key + ".locations." + l + ".actions");
                    SpawnLocation spawnLocation = new SpawnLocation(l, LocationUtils.getLocation(location), actions);

                    spawnLocations.add(spawnLocation);
                }
            }

            spawnMap.put(key, spawn);
        }
    }

    public void teleport(Player player, Spawn spawn) {
        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> {
                    SpawnLocation location = spawn.randomLocation();
                    LocationUtils.teleport(player, location);
                    plugin.getActionManager().executeActions(player, location.getActions());
                },
                plugin.getConfig().getInt("teleport-delay")
        );
    }

    public Optional<Spawn> getSpawnByIp(String ip) {
        return spawnMap.values().stream().filter(spawn -> spawn.getIps().contains(ip)).findAny();
    }

    public Optional<Spawn> getSpawnByName(String name) {
        return Optional.ofNullable(spawnMap.get(name));
    }

    public void addSpawn(String key, Spawn spawn) {
        this.spawnMap.put(key, spawn);
    }

    public Collection<Spawn> getSpawns() {
        return this.spawnMap.values();
    }

}
