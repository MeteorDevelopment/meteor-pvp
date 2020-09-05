package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.utils.Prefixes;
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

        if (Duels.INSTANCE.cancelRequest(player)) {
            player.sendMessage(Prefixes.DUELS + "Cancelled duel request.");
        } else {
            player.sendMessage(Prefixes.DUELS + "You don't have any active sent duel request.");
        }

        return true;
    }
}
