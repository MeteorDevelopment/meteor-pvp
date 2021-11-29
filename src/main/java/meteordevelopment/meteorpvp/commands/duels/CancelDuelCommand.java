package meteordevelopment.meteorpvp.commands.duels;

import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.utils.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelDuelCommand extends MyCommand {
    public CancelDuelCommand() {
        super("cancelduel", "Cancels your duel request.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (Duels.INSTANCE.cancelRequest(player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.cancelledRequest());
        }
        else {
            player.sendMessage(Prefixes.DUELS + Msgs.didntSendRequest());
        }

        return true;
    }
}
