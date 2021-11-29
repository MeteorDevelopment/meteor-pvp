package meteordevelopment.meteorpvp.duels;

import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DuelsGui {
    public static final String TITLE = "Select duel mode, ";

    public static final String OVERWORLD = ChatColor.GREEN + "Overworld";
    public static final String OVERWORLD_FLAT = ChatColor.GREEN + "Overworld flat";

    public static final String NETHER = ChatColor.RED + "Nether";
    public static final String NETHER_FLAT = ChatColor.RED + "Nether flat";

    public static Inventory create(Player player, Player receiver) {
        Inventory gui = Bukkit.createInventory(player, 9, TITLE + receiver.getName());

        item(gui, 1, Material.GRASS_BLOCK, OVERWORLD, Duels.INSTANCE.overworldNormal);
        item(gui, 2, Material.GRASS_BLOCK, OVERWORLD_FLAT, Duels.INSTANCE.overworldFlat);

        item(gui, 6, Material.NETHERRACK, NETHER, Duels.INSTANCE.netherNormal);
        item(gui, 7, Material.NETHERRACK, NETHER_FLAT, Duels.INSTANCE.netherFlat);

        Utils.fillPanes(gui);
        return gui;
    }

    private static void item(Inventory gui, int slot, Material item, String name, DuelsMode mode) {
        ItemStack itemStack = new ItemStack(item);
        Utils.setName(itemStack, name);

        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = new ArrayList<>(1);
        lore.add(mode.getAvailableCount() + " available arenas");

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(slot, itemStack);
    }
}
