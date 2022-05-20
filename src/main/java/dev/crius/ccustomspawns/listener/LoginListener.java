package dev.crius.ccustomspawns.listener;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.spawn.Spawn;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Optional;

public class LoginListener implements Listener {

    private final CCustomSpawnsPlugin plugin;

    public LoginListener(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) return;

        String ip = event.getHostname().split(":")[0];
        if (ip.isEmpty()) return;

        plugin.debug("Someone joined, getting the spawn with IP: " + ip);
        Optional<Spawn> spawn = plugin.getSpawnManager().getSpawnByIp(ip);
        Optional<Spawn> defSpawn = plugin.getSpawnManager().getSpawns().stream().filter(s -> s.getIps().isEmpty()).findAny();
        Player player = event.getPlayer();

        if (defSpawn.isPresent() && (!spawn.isPresent() ||
                spawn.get().getPermission() != null && !player.hasPermission(spawn.get().getPermission()))) {
            plugin.getSpawnManager().teleport(player, defSpawn.get());
            return;
        }

        spawn.ifPresent(value -> plugin.getSpawnManager().teleport(player, value));
    }

}
