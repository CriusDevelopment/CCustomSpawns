package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.action.Action;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class ActionBarAction implements Action {

    private final CCustomSpawnsPlugin plugin;

    public ActionBarAction(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void process(String str, Player player) {
        plugin.adventure().player(player).sendActionBar(MiniMessage.miniMessage().deserialize(str));
    }

}
