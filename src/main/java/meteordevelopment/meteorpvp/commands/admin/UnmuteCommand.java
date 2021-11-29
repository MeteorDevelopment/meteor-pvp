package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.chat.Mutes;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand extends MyCommand {
    public UnmuteCommand() {
        super("unmute", "Unmutes a player.", "/unmute <username>", Perms.HELPER);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        Player toUnmute = Bukkit.getPlayer(args[0]);
        if (toUnmute == null) {
            sender.sendMessage(Prefixes.MUTES + Msgs.playerNotOnline());
            return true;
        }

        if (Mutes.removeMute(toUnmute)) {
            Bukkit.broadcastMessage(Prefixes.MUTES + Msgs.broadcastUnmute(toUnmute));
        }
        else {
            sender.sendMessage(Prefixes.MUTES + Msgs.notMuted());
        }

        return true;
    }
}
