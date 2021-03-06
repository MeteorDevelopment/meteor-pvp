package minegame159.meteorpvp.commands.duels;

import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.duels.Duels;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import minegame159.meteorpvp.utils.Regions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelDuelCommand extends MyCommand {
    public CancelDuelCommand() {
        super("cancelduel", "Cancels your duel request.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (Regions.isInAnyPvp(player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.cantUseThisInPvp());
            return true;
        }

        if (Duels.INSTANCE.cancelRequest(player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.cancelledRequest());
        } else {
            player.sendMessage(Prefixes.DUELS + Msgs.didntSendRequest());
        }

        return true;
    }
}
