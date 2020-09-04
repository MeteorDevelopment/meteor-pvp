package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.duels.DuelArena;
import minegame159.thebestplugin.duels.Duels;
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
            player.sendMessage(Duels.MSG_PREFIX + "Player not online.");
            return true;
        }

        if (receiver == player) {
            player.sendMessage(Duels.MSG_PREFIX + "You can't duel yourself.");
            return true;
        }

        DuelArena duel = TheBestPlugin.DUELS.getDuel(player);
        if (duel != null) {
            player.sendMessage(Duels.MSG_PREFIX + "Player is in duel with " + duel.getOther(player).getName() + ".");
            return true;
        }

        if (TheBestPlugin.DUELS.sendRequest(player, receiver)) {
            player.sendMessage(Duels.MSG_PREFIX + "Duel request sent.");
        } else {
            player.sendMessage(Duels.MSG_PREFIX + "You already have active duel request.");
            player.sendMessage(Duels.MSG_PREFIX + "Do /cancelduel to cancel it.");
        }

        return true;
    }
}
