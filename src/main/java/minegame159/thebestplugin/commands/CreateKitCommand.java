package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class CreateKitCommand implements CommandExecutor, TabCompleter {
    public static final Permission UNLIMITED_KITS_PERM = new Permission("thebestplugin.unlimited-kits");

    private static final List<String> EMPTY = new ArrayList<>(0);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        if (!Kits.INSTANCE.hasKitWithName(args[0])) {
            int kitCount = Kits.INSTANCE.getCount(player);

            if (player.hasPermission(UNLIMITED_KITS_PERM) || kitCount < 3) {
                Kits.INSTANCE.addKit(new Kit(args[0], player));
                sender.sendMessage(Kits.MSG_PREFIX + "Created kit with name " + ChatColor.GRAY + "'" + args[0] + "'" + ChatColor.WHITE + ".");
            } else {
                sender.sendMessage(Kits.MSG_PREFIX + "You can only have 3 kits.");
            }
        } else {
            sender.sendMessage(Kits.MSG_PREFIX + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "already exists.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return EMPTY;
    }
}
