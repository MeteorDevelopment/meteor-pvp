package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kit;
import minegame159.thebestplugin.Kits;
import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteKitCommand extends MyCommand {
    public static final List<String> EMPTY = new ArrayList<>(0);

    public DeleteKitCommand() {
        super("deletekit", "Deletes a kit.", "/deletekit <name>", null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        Kit kit = Kits.INSTANCE.getKit(args[0]);
        if (kit != null) {
            if (kit.author.equals(player.getUniqueId()) || player.hasPermission(Perms.DELETE_KIT)) {
                Kits.INSTANCE.deleteKit(args[0]);
                sender.sendMessage(Prefixes.KITS + Msgs.deletedKit(args[0]));
            } else {
                sender.sendMessage(Prefixes.KITS + Msgs.doesntOwnKit());
            }
        } else {
            sender.sendMessage(Prefixes.KITS + Msgs.kitDoesntExist(args[0]));
        }

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return Kits.INSTANCE.getNames((Player) sender);
        }

        return EMPTY;
    }
}
