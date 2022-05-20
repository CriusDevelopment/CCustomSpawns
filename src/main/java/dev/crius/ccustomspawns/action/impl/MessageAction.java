package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.action.Action;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class MessageAction implements Action {

    private final CCustomSpawnsPlugin plugin;

    public MessageAction(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void process(String str, Player player) {
        plugin.adventure().player(player).sendMessage(MiniMessage.miniMessage().deserialize(str));
    }

}
