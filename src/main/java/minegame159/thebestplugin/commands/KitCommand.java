package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {
    private static final List<String> EMPTY = new ArrayList<>(0);

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        Kit kit = Kits.INSTANCE.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(Kits.MSG_PREFIX + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "doesn't exist.");
        } else {
            kit.apply(player);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Kits.INSTANCE.getNames();
        }

        return EMPTY;
    }
}
