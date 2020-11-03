package minegame159.thebestplugin.commands.kits;

import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.kits.Kit;
import minegame159.thebestplugin.kits.Kits;
import minegame159.thebestplugin.commands.MyCommand;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Regions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends MyCommand {
    public KitCommand() {
        super("kit", "Opens kit gui.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (Regions.isInAnyPvp(player)) {
            player.sendMessage(Prefixes.KITS + Msgs.cantUseThisInPvp());
            return true;
        }

        if (args.length == 1) {
            Kit kit = Kits.INSTANCE.get(args[0]);
            if (kit == null) player.sendMessage(Prefixes.KITS + Msgs.kitDoesntExist(args[0]));
            else {
                if (player.hasPermission(Perms.ADMIN) || kit.author.equals(player.getUniqueId())) kit.apply(player);
                else player.sendMessage(Prefixes.KITS + Msgs.dontOwnKit());
            }
            return true;
        }

        player.openInventory(Kits.INSTANCE.guiMain(player));
        return true;
    }
}
