package meteordevelopment.meteorpvp.commands.duels;

import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.duels.Duel;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.duels.DuelsGui;
import meteordevelopment.meteorpvp.chat.Msgs;
import meteordevelopment.meteorpvp.chat.Prefixes;
import meteordevelopment.meteorpvp.arenas.Regions;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand extends MyCommand {
    public DuelCommand() {
        super("duel", "Sends duel request to a player.", "/duel <player>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

        if (Regions.isInAnyPvp(player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.cantUseThisInPvp());
            return true;
        }

        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            player.sendMessage(Prefixes.DUELS + Msgs.playerNotOnline());
            return true;
        }

        if (receiver == player) {
            player.sendMessage(Prefixes.DUELS + Msgs.cantDuelYourself());
            return true;
        }

        Duel duel = Duels.INSTANCE.get(receiver);
        if (duel != null) {
            player.sendMessage(Prefixes.DUELS + Msgs.playerIsInDuel(duel.getOther(receiver).getName()));
            return true;
        }

        if (Duels.INSTANCE.hasSentRequest(player)) {
            player.sendMessage(Prefixes.DUELS + Msgs.alreadySentRequest());
            player.sendMessage(Prefixes.DUELS + Msgs.cancelDuelHelp());
            return true;
        }

        player.openInventory(DuelsGui.create(player, receiver));
        return true;
    }
}
