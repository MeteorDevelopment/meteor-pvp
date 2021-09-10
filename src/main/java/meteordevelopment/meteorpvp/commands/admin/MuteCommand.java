package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.chat.Mutes;
import meteordevelopment.meteorpvp.chat.Prefixes;
import meteordevelopment.meteorpvp.commands.MyCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand extends MyCommand {
    public MuteCommand() {
        super("mute", "Mutes a player.", "/mute <username>", Perms.HELPER);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length != 1) return false;

        OfflinePlayer toMute = Bukkit.getOfflinePlayer(args[0]);
        boolean toMuteOnline = toMute.getPlayer() != null;

        boolean senderOnline = sender instanceof Player;

        if (sender == toMute) {
            sender.sendMessage(Prefixes.MUTES + "You cannot mute yourself.");
            return true;
        }

        if (senderOnline
                && sender.hasPermission(Perms.ADMIN)
                && toMuteOnline
                && toMute.getPlayer().hasPermission(Perms.ADMIN)) {
            sender.sendMessage(Prefixes.MUTES + "You cannot mute another admin.");
            return true;
        }

        if (senderOnline
                && sender.hasPermission(Perms.MODERATOR)
                && toMuteOnline
                && (toMute.getPlayer().hasPermission(Perms.ADMIN)
                || toMute.getPlayer().hasPermission(Perms.MODERATOR))) {
            sender.sendMessage(Prefixes.MUTES + "You cannot mute another moderator.");
            return true;
        }

        if (senderOnline
                && sender.hasPermission(Perms.HELPER)
                && toMuteOnline
                && (toMute.getPlayer().hasPermission(Perms.ADMIN)
                || toMute.getPlayer().hasPermission(Perms.MODERATOR)
                || toMute.getPlayer().hasPermission(Perms.HELPER))) {
            sender.sendMessage(Prefixes.MUTES + "You cannot mute another helper.");
            return true;
        }

        if (Mutes.addMute(toMute)) {
            Bukkit.broadcast(Component.text(Prefixes.MUTES + String.format("%s%s%s has been muted.", ChatColor.RED, toMute.getName(), ChatColor.GRAY)));
        }
        else {
            sender.sendMessage(Prefixes.MUTES + "That player is already muted.");
        }

        return true;
    }
}
