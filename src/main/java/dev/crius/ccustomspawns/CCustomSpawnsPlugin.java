package dev.crius.ccustomspawns;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.crius.ccustomspawns.action.ActionManager;
import dev.crius.ccustomspawns.command.CSpawnsCommand;
import dev.crius.ccustomspawns.configuration.Configuration;
import dev.crius.ccustomspawns.gui.EditGui;
import dev.crius.ccustomspawns.gui.EditSLocationGui;
import dev.crius.ccustomspawns.listener.ChatInputListener;
import dev.crius.ccustomspawns.listener.LoginListener;
import dev.crius.ccustomspawns.spawn.SpawnManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class CCustomSpawnsPlugin extends JavaPlugin {

    private final Cache<UUID, Consumer<String>> inputCache = CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.MINUTES).build();
    private final Configuration config = new Configuration(this, "config.yml");
    private final Configuration data = new Configuration(this, "spawns.yml");
    private final ActionManager actionManager = new ActionManager(this);
    private final SpawnManager spawnManager = new SpawnManager(this);
    private boolean debugMode = true;
    private BukkitAudiences adventure;

    @Override
    public void onEnable() {

        this.config.create();
        this.data.create();

        //System.setProperty("paperlib.shown-benefits", "1");
        //PaperLib.suggestPaper(this);

        this.adventure = BukkitAudiences.create(this);

        EditGui.initialize(this);
        EditSLocationGui.initialize(this);

        // we will load the spawns after a tick so the worlds will be loaded
        Bukkit.getScheduler().runTask(this, this.spawnManager::load);

        this.getServer().getPluginManager().registerEvents(new LoginListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ChatInputListener(this), this);

        CSpawnsCommand command = new CSpawnsCommand(this);
        this.getCommand("cspawns").setExecutor(command);
        this.getCommand("cspawns").setTabCompleter(command);

        debugMode = config.getBoolean("debug-mode");
    }

    @Override
    public void onDisable() {
        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public void debug(String str) {
        if (debugMode) this.getLogger().info("[DEBUG] " + str);
    }

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }

        return this.adventure;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    @Override
    @NotNull
    public Configuration getConfig() {
        return config;
    }

    public Configuration getData() {
        return data;
    }

    public Cache<UUID, Consumer<String>> getInputCache() {
        return inputCache;
    }
}
