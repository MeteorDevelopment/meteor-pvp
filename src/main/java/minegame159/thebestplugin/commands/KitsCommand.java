package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import net.royawesome.jlibnoise.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

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

        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                page = MathHelper.clamp(page, 1, Kits.INSTANCE.getMaxPage());
            } catch (NumberFormatException ignored) {
                page = 1;
            }
        }

        Inventory gui = Bukkit.createInventory(player, 9 * 6, Kits.GUI_TITLE);
        Kits.INSTANCE.fillGui(gui, page);

        player.openInventory(gui);
        return true;
    }
}
