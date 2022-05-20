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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;

public class EditSLocationGui {

    private static CCustomSpawnsPlugin plugin;

    public static void initialize(CCustomSpawnsPlugin plugin) {
        EditSLocationGui.plugin = plugin;
    }

    public static void open(Player player, Spawn spawn) {
        Component component = Component.text("Editing Locations of " + spawn.getName())
                .decoration(TextDecoration.UNDERLINED, true);
        Gui gui = Gui.gui().rows(3).disableAllInteractions().title(component).create();

        for (int i = 0; i<spawn.getLocations().size(); i++) {
            SpawnLocation spawnLocation = spawn.getLocations().get(i);
            GuiItem spawnItem = ItemBuilder.from(Material.ENDER_PEARL)
                    .setName(ColorUtils.colored("&6Edit " + spawnLocation.getName()))
                    .setLore(
                            ColorUtils.colored("&7To update the location to current location: Right Click"),
                            ColorUtils.colored("&7To add an action: Left Click ( left click and shift to empty )"),
                            ColorUtils.colored("&7To delete this location: Middle Click")
                    ).asGuiItem(event -> {
                        if (event.isRightClick()) {
                            spawnLocation.setLocation(player.getLocation());
                            plugin.getData().set("Spawns." + spawn.getName() + ".locations."
                                            + spawnLocation.getName() + ".location", LocationUtils.getLocation(player.getLocation()));
                            plugin.getData().save();
                            return;
                        }

                        if (event.getClick() == ClickType.MIDDLE) {
                            spawn.getLocations().remove(spawnLocation);
                            plugin.getData().set("Spawns." + spawn.getName() + ".locations." + spawnLocation.getName(), null);
                            plugin.getData().save();
                            open(player, spawn);
                            return;
                        }

                        if (event.isLeftClick() && !event.isShiftClick()) {
                            gui.close(player);
                            player.sendMessage(ChatColor.GRAY + "Please type the action you want to add.");
                            player.sendMessage(ChatColor.GRAY + "Type 'cancel' to cancel this action.");

                            Consumer<String> consumer = (action) -> {
                                if (!plugin.getActionManager().isValidAction(action)) {
                                    player.sendMessage(ChatColor.RED + "Not a valid action!");
                                    return;
                                }

                                player.sendMessage(ChatColor.GREEN + "Successfully added an action: " + action);
                                spawnLocation.getActions().add(action);

                                plugin.getData().set("Spawns." + spawn.getName()
                                        + ".locations." + spawnLocation.getName() + ".actions", spawnLocation.getActions());
                                plugin.getData().save();
                                open(player, spawn);
                            };

                            plugin.getInputCache().put(player.getUniqueId(), consumer);
                            return;
                        }

                        if (event.isLeftClick() && event.isShiftClick()) {
                            player.sendMessage(ChatColor.GREEN + "Successfully empty the action list.");
                            spawnLocation.getActions().clear();

                            plugin.getData().set("Spawns." + spawn.getName()
                                    + ".locations." + spawnLocation.getName() + ".actions", spawnLocation.getActions());
                            plugin.getData().save();
                            return;
                        }

                        player.sendMessage(ChatColor.RED + "Could not recognise the click type. Follow the introductions.");
                    });

            gui.setItem(i, spawnItem);
        }

        gui.open(player);
    }

}
