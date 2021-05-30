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

public class MuteCommand extends MyCommand {

    public MuteCommand() {
        super("mute", "Mutes a player.", "/mute <username>", Perms.MODERATOR);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        Player toMute = Bukkit.getPlayer(args[0]);

        if (toMute == null) {
            if (sender instanceof Player) sender.sendMessage(Prefixes.MUTES + Msgs.playerNotOnline());
            return true;
        }

        if (sender == toMute) {
            sender.sendMessage(Prefixes.MUTES + "You cannot mute yourself.");
            return true;
        }

        if (Mutes.addMute(toMute)) {
            Bukkit.broadcastMessage(Prefixes.MUTES + String.format("%s%s%s has been muted.", ChatColor.RED, toMute.getName(), ChatColor.GRAY));
        } else {
            if (sender instanceof Player) sender.sendMessage(Prefixes.MUTES + "That player is already muted.");
        }

        return true;
    }

}
