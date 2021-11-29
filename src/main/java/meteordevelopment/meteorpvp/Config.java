package meteordevelopment.meteorpvp;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {
    // Files
    public static File FOLDER;
    public static File CONFIG_FILE;
    public static FileConfiguration CONFIG;

    // Kit creator
    public static Location KIT_CREATOR_LOCATION;
    public static boolean KIT_CREATOR_ENABLED = true;

    // Anti cheat
    public static int MIN_CRYSTAL_AGE;

    public static void init() {
        FOLDER = MeteorPvp.INSTANCE.getDataFolder();
        CONFIG_FILE = new File(FOLDER, "config.yml");

        // Config
        MeteorPvp.INSTANCE.saveDefaultConfig();
        CONFIG = MeteorPvp.INSTANCE.getConfig();

        // Kit creator
        KIT_CREATOR_LOCATION = new Location(Bukkit.getWorld("world"), 69420.5, 100, 69420.5, 0, 0);
        KIT_CREATOR_ENABLED = CONFIG.getBoolean("kit_creator.enabled");

        // Anti cheat
        MIN_CRYSTAL_AGE = CONFIG.getInt("anti_cheat.min_crystal_age");
    }

    public static void save() {
        try {
            CONFIG.save(CONFIG_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
