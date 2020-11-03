package minegame159.meteorpvp.kits;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KitStats {
    public static int totems, egaps, xpBottles, obsidian, crystals, beds;

    public static void count(Kit kit) {
        totems = egaps = xpBottles = obsidian = crystals = beds = 0;

        for (ItemStack itemStack : kit.items) {
            if (itemStack == null) continue;

            Material item = itemStack.getType();
            int count = itemStack.getAmount();

            if (item == Material.TOTEM_OF_UNDYING) totems += count;
            else if (item == Material.ENCHANTED_GOLDEN_APPLE) egaps += count;
            else if (item == Material.EXPERIENCE_BOTTLE) xpBottles += count;
            else if (item == Material.OBSIDIAN) obsidian += count;
            else if (item == Material.END_CRYSTAL) crystals += count;
            else if (item.getKey().getKey().endsWith("_bed")) beds += count;
        }
    }
}
