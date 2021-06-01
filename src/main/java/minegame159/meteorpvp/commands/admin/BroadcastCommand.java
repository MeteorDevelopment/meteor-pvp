package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.ArenaClearer;
import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class BroadcastCommand extends MyCommand {
    public BroadcastCommand() {
        super("broadcast", "Broadcasts a message..", Perms.MODERATOR);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 1) return true;
        Bukkit.broadcastMessage(Prefixes.MPVP + ChatColor.GRAY + String.join(" ", args));
        return true;
    }

}