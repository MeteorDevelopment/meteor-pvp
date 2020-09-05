package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.duels.Duels;
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

        Player player2 = Bukkit.getPlayer(args[0]);
        if (player2 == null) {
            player.sendMessage(Duels.MSG_PREFIX + "Player not online.");
            return true;
        }

        if (!Duels.INSTANCE.decline(player2, player)) {
            player.sendMessage(Duels.MSG_PREFIX + "You don't have pending request from that player.");
        }

        return true;
    }
}
