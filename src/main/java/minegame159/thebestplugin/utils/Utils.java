package minegame159.thebestplugin.utils;

import minegame159.thebestplugin.Perms;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

    public static boolean isDonator(HumanEntity player) {
        return player.hasPermission(Perms.DONATOR);
    }

    public static void setName(ItemStack itemStack, String name) {
        ItemMeta pageItemMeta = itemStack.getItemMeta();
        pageItemMeta.setDisplayName(name);
        itemStack.setItemMeta(pageItemMeta);
    }
}
