package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.action.Action;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;

public class SubtitleAction implements Action {

    private final CCustomSpawnsPlugin plugin;

    public SubtitleAction(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void process(String str, Player player) {
        plugin.adventure().player(player).sendTitlePart(TitlePart.SUBTITLE, MiniMessage.miniMessage().deserialize(str));
    }

}
