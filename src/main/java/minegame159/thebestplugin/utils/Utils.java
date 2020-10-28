package minegame159.thebestplugin.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Utils {
    public static World OVERWORLD, NETHER;

    public static void onEnable() {
        OVERWORLD = Bukkit.getWorld("world");
        NETHER = Bukkit.getWorld("world_nether");
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
