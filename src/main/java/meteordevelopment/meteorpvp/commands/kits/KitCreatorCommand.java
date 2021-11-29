package meteordevelopment.meteorpvp.commands.kits;

import meteordevelopment.meteorpvp.Config;
import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.utils.Prefixes;
import meteordevelopment.meteorpvp.commands.MyCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreatorCommand extends MyCommand {
    public KitCreatorCommand() {
        super("kitcreator", "Teleports you to kit creator.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (Regions.isInAnyPvp(player)) {
            sender.sendMessage(Prefixes.ARENA + Msgs.cantUseThisInPvp());
            return true;
        }

        player.teleport(Config.KIT_CREATOR_LOCATION);

        return true;
    }
}
