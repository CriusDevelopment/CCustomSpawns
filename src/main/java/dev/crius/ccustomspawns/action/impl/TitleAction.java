package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.action.Action;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;

public class TitleAction implements Action {

    private final CCustomSpawnsPlugin plugin;

    public TitleAction(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void process(String str, Player player) {
        //final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        //final Title title = Title.title(MiniMessage.miniMessage().deserialize(str), Component.empty(), times);
        //plugin.adventure().player(player).showTitle(title);

        plugin.adventure().player(player).sendTitlePart(TitlePart.TITLE, MiniMessage.miniMessage().deserialize(str));
    }

}
