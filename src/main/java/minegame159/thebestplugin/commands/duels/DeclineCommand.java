package minegame159.thebestplugin.commands.duels;

import minegame159.thebestplugin.commands.MyCommand;
import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Regions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeclineCommand extends MyCommand {
    public DeclineCommand() {
        super("decline", "Declines a duel.", null, null);
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

        if (!Duels.INSTANCE.decline(player2, player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.dontHaveRequest());
        }

        return true;
    }
}