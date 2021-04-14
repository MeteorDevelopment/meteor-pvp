package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.Mutes;
import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand extends MyCommand {

    public UnmuteCommand() {
        super("unmute", "Unmutes a player.", null, Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        Player toUnmute = Bukkit.getPlayer(args[0]);

        if (toUnmute == null) {
            if (sender instanceof Player) sender.sendMessage(Prefixes.MUTES + Msgs.playerNotOnline());
            return true;
        }

        if (sender == toUnmute) {
            sender.sendMessage(Prefixes.MUTES + "You cannot unmute yourself.");
            return true;
        }

        if (Mutes.removeMute(toUnmute)) {
            Bukkit.broadcastMessage(Prefixes.MUTES + String.format("%s%s%s has been unmuted.", ChatColor.RED, toUnmute.getName(), ChatColor.GRAY));
        } else {
            if (sender instanceof Player) sender.sendMessage(Prefixes.MUTES + "That player is not muted.");
        }

        return true;
    }

}
