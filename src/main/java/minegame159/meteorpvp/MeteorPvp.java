package minegame159.meteorpvp;

import minegame159.meteorpvp.commands.Commands;
import minegame159.meteorpvp.duels.Duels;
import minegame159.meteorpvp.kits.Kits;
import minegame159.meteorpvp.listeners.Listeners;
import minegame159.meteorpvp.utils.Regions;
import minegame159.meteorpvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MeteorPvp extends JavaPlugin implements Listener {
    public static MeteorPvp INSTANCE;

    public static String WEBSERVER_TOKEN;

    public static Location KIT_CREATOR_LOCATION;

    public static File CONFIG_FOLDER;

    public static boolean KIT_CREATOR_ENABLED = true;

    @Override
    public void onEnable() {
        INSTANCE = this;

        saveDefaultConfig();
        WEBSERVER_TOKEN = getConfig().getString("webserver_config");

        CONFIG_FOLDER = getDataFolder();
        CONFIG_FOLDER.mkdirs();

        KIT_CREATOR_LOCATION = new Location((Bukkit.getWorld("world")), 100000, 101, 100000);

        Perms.register();
        Commands.register();
        Listeners.register();

        Utils.onEnable();
        Regions.onEnable();
        ArenaClearer.onEnable();

        Kits.INSTANCE.init();
        Duels.INSTANCE.init();

        WebServer.enable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        WebServer.disable();
    }
}
