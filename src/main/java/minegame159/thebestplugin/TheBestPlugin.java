package minegame159.thebestplugin;

import minegame159.thebestplugin.commands.*;
import minegame159.thebestplugin.utils.Uptime;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TheBestPlugin extends JavaPlugin implements Listener {
    public static File CONFIG_FOLDER;

    @Override
    public void onEnable() {
        CONFIG_FOLDER = getDataFolder();
        CONFIG_FOLDER.mkdirs();

        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new Kits(), this);

        Bukkit.getScheduler().runTaskTimer(this, TabList::update, 0, 80);

        Bukkit.getPluginManager().addPermission(CreateKitCommand.UNLIMITED_KITS_PERM);
        Bukkit.getPluginManager().addPermission(DeleteKitCommand.DELETE_KIT_PERM);

        getCommand("gmc").setExecutor(new GmcCommand());
        getCommand("gms").setExecutor(new GmsCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("kits").setExecutor(new KitsCommand());
        getCommand("createkit").setExecutor(new CreateKitCommand());
        getCommand("kit").setExecutor(new KitCommand());
        getCommand("deletekit").setExecutor(new DeleteKitCommand());
        getCommand("spawn").setExecutor(new SpawnCommand());
        getCommand("kitcreator").setExecutor(new KitCreatorCommand());
        getCommand("suicide").setExecutor(new SuicideCommand());

        Uptime.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TabList.update();
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        TabList.update();
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        TabList.update();
    }

    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().equalsIgnoreCase("/help")) {
            event.setCancelled(true);
            event.getPlayer().performCommand("help thebestplugin");
        }
    }
}
