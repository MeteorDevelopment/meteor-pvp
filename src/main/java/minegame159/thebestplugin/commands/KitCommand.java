package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand extends MyCommand {
    private static final List<String> EMPTY = new ArrayList<>(0);

    public KitCommand() {
        super("kit", "Gives you a kit.", "/kit <name>", null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        if (!Kits.INSTANCE.useKitCommand(player)) {
            player.sendMessage(Kits.MSG_PREFIX + "Can't use this. Do /suicide");
            return true;
        }

        Kit kit = Kits.INSTANCE.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(Kits.MSG_PREFIX + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "doesn't exist.");
        } else {
            kit.apply(player);
        }

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) {
            return Kits.INSTANCE.getNames();
        }

        return EMPTY;
    }
}
