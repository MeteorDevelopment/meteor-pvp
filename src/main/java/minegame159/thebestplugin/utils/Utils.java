package minegame159.thebestplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static World OVERWORLD, NETHER;

    private static long startTime;

    public static void onEnable() {
        OVERWORLD = Bukkit.getWorld("world");
        NETHER = Bukkit.getWorld("world_nether");

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

    public static void resetToSpawn(Player player) {
        player.teleport(player.getWorld().getSpawnLocation().add(0.5, 0, 0.5));

        player.getInventory().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);

        for (PotionEffect effect : player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }
    }
}
