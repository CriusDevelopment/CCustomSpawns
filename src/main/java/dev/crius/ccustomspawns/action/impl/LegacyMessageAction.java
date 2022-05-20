package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.action.Action;
import dev.crius.ccustomspawns.util.ColorUtils;
import org.bukkit.entity.Player;

public class LegacyMessageAction implements Action {

    @Override
    public void process(String str, Player player) {
        player.sendMessage(ColorUtils.colored(str));
    }

}
