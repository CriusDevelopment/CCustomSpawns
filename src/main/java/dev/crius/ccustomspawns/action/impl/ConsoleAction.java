package dev.crius.ccustomspawns.action.impl;

import dev.crius.ccustomspawns.action.Action;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ConsoleAction implements Action {

    @Override
    public void process(String str, Player player) {
        if (player != null)
            str = str.replace("{player}", player.getName());

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), str);
    }

}
