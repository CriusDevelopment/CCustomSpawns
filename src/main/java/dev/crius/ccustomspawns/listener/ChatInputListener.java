package dev.crius.ccustomspawns.listener;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Consumer;

public class ChatInputListener implements Listener {

    private final CCustomSpawnsPlugin plugin;

    public ChatInputListener(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Consumer<String> consumer = plugin.getInputCache().getIfPresent(player.getUniqueId());
        if (consumer == null) return;

        event.setCancelled(true);
        player.sendMessage(ChatColor.GREEN + "We are no longer waiting for an input.");

        if (!event.getMessage().equalsIgnoreCase("cancel")) {
            consumer.accept(event.getMessage());
        }

        plugin.getInputCache().invalidate(player.getUniqueId());
    }

}
