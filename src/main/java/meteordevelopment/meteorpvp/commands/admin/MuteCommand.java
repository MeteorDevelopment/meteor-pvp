package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.chat.Mutes;
import meteordevelopment.meteorpvp.utils.Prefixes;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.utils.Perms;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MuteCommand extends MyCommand {
    public MuteCommand() {
        super("mute", "Mutes a player.", "/mute <username>", Perms.HELPER);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length != 1) return false;

        OfflinePlayer toMute = Bukkit.getOfflinePlayer(args[0]);

        if (sender == toMute) {
            sender.sendMessage(Prefixes.MUTES + Msgs.cantMuteYourself());
            return true;
        }

        if (!canMute(sender, toMute)) {
            sender.sendMessage(Prefixes.MUTES + Msgs.cantMutePlayer());
            return true;
        }

        if (Mutes.addMute(toMute)) {
            Bukkit.broadcast(Component.text(Prefixes.MUTES + Msgs.broadcastMute(toMute)));
        }
        else {
            sender.sendMessage(Prefixes.MUTES + Msgs.alreadyMuted());
        }

        return true;
    }

    public boolean canMute(CommandSender sender, OfflinePlayer toMute) {
        if (sender instanceof ConsoleCommandSender) return true;
        if (toMute.getPlayer() == null) return true;

        Player toMutePlayer = toMute.getPlayer();

        if (sender.hasPermission(Perms.ADMIN)) {
            return !toMutePlayer.hasPermission(Perms.ADMIN);
        }
        if (sender.hasPermission(Perms.MODERATOR)) {
            return !toMutePlayer.hasPermission(Perms.ADMIN) && !toMutePlayer.hasPermission(Perms.MODERATOR);
        }
        if (sender.hasPermission(Perms.HELPER)) {
            return !toMutePlayer.hasPermission(Perms.ADMIN) && !toMutePlayer.hasPermission(Perms.MODERATOR) && toMutePlayer.hasPermission(Perms.HELPER);
        }

        return false;
    }
}
