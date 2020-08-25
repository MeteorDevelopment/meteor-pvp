package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class DeleteKitCommand implements CommandExecutor, TabCompleter {
    public static final List<String> EMPTY = new ArrayList<>(0);

    public static final Permission DELETE_KIT_PERM = new Permission("thebestplugin.delete-kit");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        Kit kit = Kits.INSTANCE.getKit(args[0]);
        if (kit != null) {
            if (kit.author.equals(player.getUniqueId()) || player.hasPermission(DELETE_KIT_PERM)) {
                Kits.INSTANCE.deleteKit(args[0]);
                sender.sendMessage(Kits.MSG_PREFIX + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "has been deleted.");
            } else {
                sender.sendMessage(Kits.MSG_PREFIX + "You don't own that kit.");
            }
        } else {
            sender.sendMessage(Kits.MSG_PREFIX + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "doesn't exist.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return Kits.INSTANCE.getNames((Player) sender);
        }

        return EMPTY;
    }
}
