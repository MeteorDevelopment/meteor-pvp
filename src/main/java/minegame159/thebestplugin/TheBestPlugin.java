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
import minegame159.thebestplugin.utils.Arenas;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class TheBestPlugin extends JavaPlugin implements Listener {
    public static TheBestPlugin INSTANCE;

    public static Location KIT_CREATOR_LOCATION;
    public static Location NETHER_LOCATION;

    public static File CONFIG_FOLDER;

    public static File STATS_FILE;
    public static Stats STATS;
    public static Duels DUELS;

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
        NETHER_LOCATION = new Location((Bukkit.getWorld("world_nether")), 0, 117, 0);

        STATS_FILE = new File(CONFIG_FOLDER, "stats.json");
        if (STATS_FILE.exists()) {
            try {
                STATS = GSON.fromJson(new FileReader(STATS_FILE), Stats.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            STATS = new Stats();
        }

        DUELS = new Duels();
        new Kits();

        Perms.register();
        Commands.register();
        Listeners.register();
        TabList.register();

        Utils.onEnable();
        Kits.INSTANCE.onEnable();
        Arenas.onEnable();
        ArenaClearer.onEnable();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }
}
