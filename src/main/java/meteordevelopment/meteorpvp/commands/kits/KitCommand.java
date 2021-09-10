package meteordevelopment.meteorpvp.commands.kits;

import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.chat.Msgs;
import meteordevelopment.meteorpvp.chat.Prefixes;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.kits.Kit;
import meteordevelopment.meteorpvp.kits.Kits;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends MyCommand {
    public KitCommand() {
        super("kit", "Opens kit gui or equips named kit.", "/kit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (Regions.isInAnyPvp(player)) {
            player.sendMessage(Prefixes.KITS + Msgs.cantUseThisInPvp());
            return true;
        }

        if (args.length == 1) {
            Kit kit = Kits.INSTANCE.get(args[0]);
            if (kit == null) player.sendMessage(Prefixes.KITS + Msgs.kitDoesntExist(args[0]));
            else {
                if (kit.isPublic || Utils.isAdmin(player) || kit.author.equals(player.getUniqueId())) kit.apply(player);
                else player.sendMessage(Prefixes.KITS + Msgs.dontOwnKit());
            }
            return true;
        }

        player.openInventory(Kits.INSTANCE.guiMain(player));
        return true;
    }
}
