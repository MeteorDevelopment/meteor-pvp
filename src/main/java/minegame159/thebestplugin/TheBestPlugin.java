package minegame159.thebestplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import minegame159.thebestplugin.commands.Commands;
import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.json.ItemStackSerializer;
import minegame159.thebestplugin.json.KitSerializer;
import minegame159.thebestplugin.json.KitsSerializer;
import minegame159.thebestplugin.json.NamespacedKeySerializer;
import minegame159.thebestplugin.listeners.Listeners;
import minegame159.thebestplugin.utils.Regions;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class TheBestPlugin extends JavaPlugin implements Listener {
    public static TheBestPlugin INSTANCE;

    public static Location KIT_CREATOR_LOCATION;

    public static File CONFIG_FOLDER;

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Kits.class, new KitsSerializer())
            .registerTypeAdapter(Kit.class, new KitSerializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeAdapter(NamespacedKey.class, new NamespacedKeySerializer())
            .create();

    public static boolean KIT_CREATOR_ENABLED = true;

    @Override
    public void onEnable() {
        INSTANCE = this;

        CONFIG_FOLDER = getDataFolder();
        CONFIG_FOLDER.mkdirs();

        KIT_CREATOR_LOCATION = new Location((Bukkit.getWorld("world")), 100000, 101, 100000);

        Perms.register();
        Commands.register();
        Listeners.register();

        Kits.INSTANCE.init();
        Duels.INSTANCE.init();

        Utils.onEnable();
        Regions.onEnable();
        ArenaClearer.onEnable();

        WebServer.enable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);

        WebServer.disable();
    }
}
