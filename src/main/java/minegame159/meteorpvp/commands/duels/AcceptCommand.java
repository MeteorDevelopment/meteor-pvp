package minegame159.meteorpvp.commands.duels;

import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.duels.Duels;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import minegame159.meteorpvp.utils.Regions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptCommand extends MyCommand {
    public AcceptCommand() {
        super("accept", "Accepts a duel.", "/accept <username>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        if (Regions.isInAnyPvp(player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.cantUseThisInPvp());
            return true;
        }

        Player player2 = Bukkit.getPlayer(args[0]);
        if (player2 == null) {
            player.sendMessage(Prefixes.DUELS + Msgs.playerNotOnline());
            return true;
        }

        if (!Duels.INSTANCE.accept(player2, player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.dontHaveRequest());
        }

        return true;
    }
}
