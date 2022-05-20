package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.action.Action;
import org.bukkit.entity.Player;

public class PCommandAction implements Action {

    @Override
    public void process(String str, Player player) {
        if (!str.startsWith("/")) str = "/" + str;

        player.performCommand(str);
    }

}
