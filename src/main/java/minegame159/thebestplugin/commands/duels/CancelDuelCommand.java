package minegame159.thebestplugin.commands.duels;

import minegame159.thebestplugin.commands.MyCommand;
import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Regions;
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
