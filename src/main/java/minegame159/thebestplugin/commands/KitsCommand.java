package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitsCommand extends MyCommand {
    public KitsCommand() {
        super("kits", "Opens a list of kits.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (!Kits.INSTANCE.useKitCommand(player)) {
            player.sendMessage(Kits.MSG_PREFIX + "Can't use this. Do /suicide");
            return true;
        }

        Inventory gui = Bukkit.createInventory(player, 9 * 6, Kits.GUI_TITLE);

        int i = 0;
        for (Kit kit : Kits.INSTANCE.getKits()) {
            if (i >= 9 * 6) break;

            ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = itemStack.getItemMeta();

            meta.setDisplayName(kit.name);
            List<String> lore = new ArrayList<>(1);
            lore.add(Bukkit.getOfflinePlayer(kit.author).getName());
            meta.setLore(lore);

            itemStack.setItemMeta(meta);
            gui.setItem(i, itemStack);

            i++;
        }

        player.openInventory(gui);
        return true;
    }
}
