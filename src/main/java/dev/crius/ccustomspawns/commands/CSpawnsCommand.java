package dev.crius.ccustomspawns.commands;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.gui.EditGui;
import dev.crius.ccustomspawns.spawn.Spawn;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSpawnsCommand implements TabExecutor {

    private final CCustomSpawnsPlugin plugin;

    public CSpawnsCommand(CCustomSpawnsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You can not use this command from console!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /cspawns create <name>");
            player.sendMessage(ChatColor.RED + "Usage: /cspawns edit <name>");
            return true;
        }

        switch (args[0].toLowerCase(Locale.ENGLISH)) {
            case "create": {
                if (args.length != 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /cspawns create <name>");
                    return true;
                }

                String name = args[1].toLowerCase(Locale.ENGLISH);
                if (plugin.getSpawnManager().getSpawnByName(name).isPresent()) {
                    player.sendMessage(ChatColor.RED + "A spawn location called " + name + " already exists!");
                    return true;
                }

                plugin.getData().set("Spawns." + name + ".ips", Collections.emptyList());
                plugin.getData().set("Spawns." + name + ".locations", Collections.emptyList());
                plugin.getData().save();

                Spawn spawn = new Spawn(name, new ArrayList<>(), new ArrayList<>(), null);
                plugin.getSpawnManager().addSpawn(name, spawn);

                player.sendMessage(ChatColor.GREEN + "Successfully created a spawn location named " + name);

                Component component = Component.text("You can edit it by clicking here. (/cspawns edit " + name + ")")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/cspawns edit " + name));
                plugin.adventure().player(player).sendMessage(component);

                break;
            }

            case "edit": {
                if (args.length != 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /cspawns edit <name>");
                    return true;
                }

                String name = args[1].toLowerCase(Locale.ENGLISH);
                Optional<Spawn> spawn = plugin.getSpawnManager().getSpawnByName(name);

                if (!spawn.isPresent()) {
                    player.sendMessage(ChatColor.RED + name + " does not exists!");
                    return true;
                }

                EditGui.open(player, spawn.get());
                plugin.adventure().player(player).playSound(
                        Sound.sound(Key.key("block.note_block.pling"), Sound.Source.BLOCK, 1, 1)
                );
            }

        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Stream.of("create", "edit").filter(s->s.toLowerCase(Locale.ENGLISH)
                    .startsWith(args[0].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
        } else if (args.length == 2) {
            return plugin.getSpawnManager().getSpawns().stream().map(Spawn::getName)
                    .filter(s->s.toLowerCase(Locale.ENGLISH).startsWith(args[1].toLowerCase(Locale.ENGLISH))).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
