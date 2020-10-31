package minegame159.thebestplugin.commands.duels;

import minegame159.thebestplugin.commands.MyCommand;
import minegame159.thebestplugin.duels.DuelArena;
import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand extends MyCommand {
    public DuelCommand() {
        super("duel", "Sends duel request to a player.", "/duel <player>", null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            player.sendMessage(Prefixes.DUELS + Msgs.playerNotOnline());
            return true;
        }

        if (receiver == player) {
            player.sendMessage(Prefixes.DUELS + Msgs.cantDuelYourself());
            return true;
        }

        DuelArena duel = Duels.INSTANCE.getDuel(player);
        if (duel != null) {
            player.sendMessage(Prefixes.DUELS + Msgs.playerIsInDuel(duel.getOther(player).getName()));
            return true;
        }

        if (Duels.INSTANCE.sendRequest(player, receiver)) {
            player.sendMessage(Prefixes.DUELS + Msgs.duelRequestSent());
        } else {
            player.sendMessage(Prefixes.DUELS + Msgs.alreadySentRequest());
            player.sendMessage(Prefixes.DUELS + Msgs.cancelDuelHelp());
        }

        return true;
    }
}
