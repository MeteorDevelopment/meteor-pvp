package meteordevelopment.meteorpvp.commands.duels;

import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeclineCommand extends MyCommand {
    public DeclineCommand() {
        super("decline", "Declines a duel.", "/decline <player>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

        Player player2 = Bukkit.getPlayer(args[0]);
        if (player2 == null) {
            player.sendMessage(Prefixes.DUELS + Msgs.playerNotOnline());
            return true;
        }

        if (!Duels.INSTANCE.decline(player2, player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.dontHaveRequest());
        }

        return true;
    }
}
