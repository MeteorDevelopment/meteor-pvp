package minegame159.thebestplugin.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.util.concurrent.TimeUnit;

public class Utils {
    private static long startTime;

    public static void onEnable() {
        startTime = System.currentTimeMillis();
    }

    public static int getUsedRamMb() {
        return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
    }

    public static int getMaxRamMb() {
        return (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
    }

    public static String getUptimeString() {
        long uptime = System.currentTimeMillis() - startTime;

        long days = TimeUnit.MILLISECONDS.toDays(uptime);
        long hours = TimeUnit.MILLISECONDS.toHours(uptime - TimeUnit.DAYS.toMillis(days));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours));

        return String.format("%dd %dh %dm", days, hours, minutes);
    }

    public static boolean isInKitCreator(Entity entity) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(Bukkit.getWorld("world")));
        if (regions == null) return false;
        ProtectedRegion kitCreatorRegion = regions.getRegion("kitcreator");

        return kitCreatorRegion != null && kitCreatorRegion.contains(BukkitAdapter.asBlockVector(entity.getLocation()));
    }
}
