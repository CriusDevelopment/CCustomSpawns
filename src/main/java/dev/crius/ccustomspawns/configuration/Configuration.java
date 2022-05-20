package dev.crius.ccustomspawns.configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Configuration extends YamlConfiguration {

    private final File file;
    private final JavaPlugin plugin;

    public Configuration(JavaPlugin plugin, String name){
        this.plugin = plugin;
        file = new File(plugin.getDataFolder(), name);

        if (file.exists())
            reload();
    }

    public void create(){
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
        }
        reload();
    }

    public void reload() {
        try {
            this.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
