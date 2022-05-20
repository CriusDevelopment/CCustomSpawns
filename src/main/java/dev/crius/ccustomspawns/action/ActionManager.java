package dev.crius.ccustomspawns.action;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.action.impl.*;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionManager {

    private final Map<String, Action> actionMap = new HashMap<>();
    private final CCustomSpawnsPlugin plugin;

    public ActionManager(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;

        actionMap.put("title", new TitleAction(plugin));
        actionMap.put("subtitle", new SubtitleAction(plugin));
        actionMap.put("message", new MessageAction(plugin));
        actionMap.put("actionbar", new ActionBarAction(plugin));
        actionMap.put("message-legacy", new LegacyMessageAction());
        actionMap.put("console", new ConsoleAction());
        actionMap.put("command", new PCommandAction());
    }

    public void executeActions(Player player, List<String> list) {
        for (String str : list) {
            String[] split = str.split(" ");
            String action = split[0].replace("[", "").replace("]", "");
            String text = str.substring(split[0].length() + 1);

            if (actionMap.containsKey(action))
                actionMap.get(action).process(text, player);
            else
                plugin.getLogger().warning("Unknown action type, skipping! Action: " + action);
        }
    }

    public boolean isValidAction(String str) {
        String[] split = str.split(" ");
        String action = split[0].replace("[", "").replace("]", "");

        return actionMap.containsKey(action);
    }

}
