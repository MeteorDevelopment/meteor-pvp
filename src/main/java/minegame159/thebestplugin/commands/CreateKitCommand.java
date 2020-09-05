package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateKitCommand extends MyCommand {
    private static final List<String> EMPTY = new ArrayList<>(0);

    public CreateKitCommand() {
        super("createkit", "Creates a new kit.", "/createkit <name>", null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        if (!Kits.INSTANCE.hasKitWithName(args[0])) {
            int kitCount = Kits.INSTANCE.getCount(player);

            if (player.hasPermission(Perms.UNLIMITED_KITS) || kitCount < 3) {
                Kits.INSTANCE.addKit(new Kit(args[0], player));
                sender.sendMessage(Prefixes.KITS + "Created kit with name " + ChatColor.GRAY + "'" + args[0] + "'" + ChatColor.WHITE + ".");
            } else {
                sender.sendMessage(Prefixes.KITS + "You can only have 3 kits.");
            }
        } else {
            sender.sendMessage(Prefixes.KITS + "Kit with name " + ChatColor.GRAY + "'" + args[0] + "' " + ChatColor.WHITE + "already exists.");
        }

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return EMPTY;
    }
}
