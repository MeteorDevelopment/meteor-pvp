package meteordevelopment.meteorpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    public static File FOLDER;
    public static FileConfiguration CONFIG;

    // Kit creator
    public static Location KIT_CREATOR_LOCATION;
    public static boolean KIT_CREATOR_ENABLED = true;

    // Anti cheat
    public static int MIN_CRYSTAL_AGE;

    // Other
    public static String SERVER_TOKEN;
    public static String UPTIME_URL;

    public static void init() {
        MeteorPvp.INSTANCE.saveDefaultConfig();

        // Config
        CONFIG = MeteorPvp.INSTANCE.getConfig();
        FOLDER = MeteorPvp.INSTANCE.getDataFolder();

        // Kit creator
        KIT_CREATOR_LOCATION = new Location(Bukkit.getWorld("world"), 100000.5, 100, 100000.5, 0, 0);
        KIT_CREATOR_ENABLED = CONFIG.getBoolean("kit_creator.enabled");

        // Anti cheat
        MIN_CRYSTAL_AGE = CONFIG.getInt("min_crystal_age");

        // Other
        SERVER_TOKEN = CONFIG.getString("server_token");
        UPTIME_URL = CONFIG.getString("uptime_url");
    }

    public static void save() {
        try {
            CONFIG.save(new File(FOLDER, "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
