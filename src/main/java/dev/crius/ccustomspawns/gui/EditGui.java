package dev.crius.ccustomspawns.gui;

import dev.crius.ccustomspawns.CCustomSpawnsPlugin;
import dev.crius.ccustomspawns.spawn.Spawn;
import dev.crius.ccustomspawns.spawn.SpawnLocation;
import dev.crius.ccustomspawns.util.ColorUtils;
import dev.crius.ccustomspawns.util.LocationUtils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditGui {

    private static CCustomSpawnsPlugin plugin;
    private static final String RIGHT_CLICK_TO_EMPTY = ColorUtils.colored("&eRight Click to empty");
    private static final Random random = new SecureRandom();

    public static void initialize(CCustomSpawnsPlugin plugin) {
        EditGui.plugin = plugin;
    }

    public static void open(Player player, Spawn spawn) {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> open(player, spawn));
            return;
        }

        Component component = Component.text("Editing " + spawn.getName()).decoration(TextDecoration.UNDERLINED, true);
        Gui gui = Gui.gui().rows(3).disableAllInteractions().title(component).create();

        // item to set the required permission
        GuiItem permissionItem = ItemBuilder.from(Material.BARRIER)
                .setName(ColorUtils.colored("&6Set a permission"))
                .setLore(ColorUtils.colored("&7Current permission: " +
                        (spawn.getPermission() == null ? "None" : spawn.getPermission())),
                        "", RIGHT_CLICK_TO_EMPTY, ColorUtils.colored("&eLeft Click to set the permission"))
                .asGuiItem(event -> {
                    if (event.isRightClick()) {
                        spawn.setPermission(null);
                        plugin.getData().set("Spawns." + spawn.getName() + ".permission", null);
                        plugin.getData().save();
                        open(player, spawn);
                        player.sendMessage(ChatColor.GREEN + "Successfully empty the required permission.");
                        return;
                    }

                    gui.close(player);
                    player.sendMessage(ChatColor.GRAY + "Please type the permission you want to set.");
                    player.sendMessage(ChatColor.GRAY + "Type 'cancel' to cancel this action.");

                    Consumer<String> consumer = (permission) -> {
                        player.sendMessage(ChatColor.GREEN + "Successfully set permission to " + permission);
                        spawn.setPermission(permission);

                        plugin.getData().set("Spawns." + spawn.getName() + ".permission", permission);
                        plugin.getData().save();
                        open(player, spawn);
                    };

                    plugin.getInputCache().put(player.getUniqueId(), consumer);

                });
        gui.setItem(11, permissionItem);
        //

        // item to add an ip

        List<String> lore = new ArrayList<>();
        lore.add(ColorUtils.colored("&7Current IPs:"));
        lore.add("");
        lore.addAll(spawn.getIps().stream().map(s-> ColorUtils.colored(" &7- " + s)).collect(Collectors.toList()));
        lore.add("");
        lore.add(RIGHT_CLICK_TO_EMPTY);
        lore.add(ColorUtils.colored("&eLeft Click to add an ip address"));
        GuiItem addIpItem = ItemBuilder.from(Material.PAPER)
                .setName(ColorUtils.colored("&6Add an IP Address"))
                .setLore(lore)
                .asGuiItem(event -> {

                    if (event.isRightClick()) {
                        spawn.getIps().clear();
                        plugin.getData().set("Spawns." + spawn.getName() + ".ips", Collections.emptyList());
                        plugin.getData().save();
                        open(player, spawn);
                        player.sendMessage(ChatColor.GREEN + "Successfully empty the IP list");
                        return;
                    }

                    gui.close(player);
                    player.sendMessage(ChatColor.GRAY + "Please type the IP Address you want to add.");
                    player.sendMessage(ChatColor.GRAY + "Type 'cancel' to cancel this action.");

                    Consumer<String> consumer = (ip) -> {
                        if (ip.contains(" ") || !ip.contains(".")) {
                            player.sendMessage(ChatColor.RED + "Invalid IP Address!");
                            open(player, spawn);
                            return;
                        }

                        player.sendMessage(ChatColor.GREEN + "Successfully added the IP: " + ip);
                        spawn.getIps().add(ip);
                        plugin.getData().set("Spawns." + spawn.getName() + ".ips", spawn.getIps());
                        plugin.getData().save();
                        open(player, spawn);
                    };

                    plugin.getInputCache().put(player.getUniqueId(), consumer);
                });
        gui.setItem(13, addIpItem);
        //

        // item to see the spawn locations
        GuiItem addActionItem = ItemBuilder.from(Material.ENDER_PEARL)
                .setName(ColorUtils.colored("&6Spawn Locations"))
                .setLore(ColorUtils.colored("&eLeft Click to open the menu"), ColorUtils.colored("&eMiddle Click to add a new one"), RIGHT_CLICK_TO_EMPTY)
                .asGuiItem(event -> {
                    if (event.isRightClick()) {
                        spawn.getLocations().clear();
                        plugin.getData().set("Spawns." + spawn.getName() + ".locations", Collections.emptyList());
                        plugin.getData().save();
                        player.sendMessage(ChatColor.GREEN + "Successfully empty the locations");
                        return;
                    }

                    if (event.getClick() == ClickType.MIDDLE) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i<6; i++) builder.append(random.nextInt(9));

                        plugin.getData().set("Spawns." + spawn.getName()
                                + ".locations." + builder + ".location", LocationUtils.getLocation(player.getLocation()));
                        plugin.getData().set("Spawns." + spawn.getName()
                                + ".locations." + builder + ".actions", new ArrayList<>());
                        plugin.getData().save();

                        spawn.getLocations().add(new SpawnLocation(builder.toString(), player.getLocation(), new ArrayList<>()));

                        player.sendMessage(ChatColor.GREEN + "Successfully created a new spawn location.");

                        EditSLocationGui.open(player, spawn);
                        return;
                    }

                    EditSLocationGui.open(player, spawn);
                });
        gui.setItem(15, addActionItem);
        //

        gui.open(player);
    }

}
