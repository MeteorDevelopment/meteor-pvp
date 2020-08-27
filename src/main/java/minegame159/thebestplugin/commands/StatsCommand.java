package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatsCommand implements CommandExecutor {
    public static final String GUI_TITLE = "Global Stats";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        Inventory gui = Bukkit.createInventory(player, 9, GUI_TITLE);

        setItem(gui, 1, Material.DIAMOND_SWORD, "Deaths: ", TheBestPlugin.STATS.deaths);
        setItem(gui, 3, Material.END_CRYSTAL, "Blown Crystals: ", TheBestPlugin.STATS.blownCrystals);
        setItem(gui, 5, Material.TOTEM_OF_UNDYING, "Popped Totems: ", TheBestPlugin.STATS.poppedTotems);
        setItem(gui, 7, Material.ARROW, "Played Duels: ", TheBestPlugin.STATS.playedDuels);

        player.openInventory(gui);
        return true;
    }

    private void setItem(Inventory gui, int slot, Material item, String name, int count) {
        ItemStack itemStack = new ItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.AQUA + name + ChatColor.WHITE + count);
        itemStack.setItemMeta(itemMeta);
        gui.setItem(slot, itemStack);
    }
}
