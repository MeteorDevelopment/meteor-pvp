package minegame159.meteorpvp.utils;

import minegame159.meteorpvp.Perms;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
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
        if (!player.isValid() || player.isDead()) return;

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

    public  static void fillPanes(Inventory gui) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = gui.getItem(i);
            if (itemStack != null && itemStack.getType() != Material.AIR) continue;

            itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            Utils.setName(itemStack, "");
            gui.setItem(i, itemStack);
        }
    }
}
