package meteordevelopment.meteorpvp.commands.normal;

import meteordevelopment.meteorpvp.chat.PrivateMessages;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageCommand extends MyCommand {
    private final StringBuilder sb = new StringBuilder();

    public MessageCommand() {
        super("msg", "Messages a person.", "/msg <name> <message>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player) || args.length < 2) return false;

        Player receiver = Bukkit.getPlayer(args[0]);
        if (receiver == null) {
            player.sendMessage(Prefixes.MPVP + ChatColor.WHITE + args[0] + ChatColor.GRAY + " is not online");
            return true;
        }

        for (int i = 1; i < args.length; i++) {
            if (i > 1) sb.append(" ");
            sb.append(args[i]);
        }

        PrivateMessages.send(player, receiver, sb.toString());
        sb.setLength(0);

        return true;
    }
}
