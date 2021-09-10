package meteordevelopment.meteorpvp.commands.normal;

import meteordevelopment.meteorpvp.chat.Ignores;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.chat.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class IgnoreCommand extends MyCommand {
    private final StringBuilder sb = new StringBuilder();

    public IgnoreCommand() {
        super("ignore", "Hides a players messages.", "/ignore <username>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            sender.sendMessage(Prefixes.MPVP + "To add/remove people do " + ChatColor.WHITE + " /ignore <name>");

            sb.append(Prefixes.MPVP).append("Currently ignoring: ").append(ChatColor.WHITE);
            int i = 0;
            for (UUID ignored : Ignores.getIgnores(player)) {
                if (i > 0) sb.append(", ");
                sb.append(Bukkit.getOfflinePlayer(ignored).getName());
                i++;
            }

            player.sendMessage(sb.toString());
            sb.setLength(0);
        }
        else if (args.length == 1) {
            OfflinePlayer toIgnore = Bukkit.getOfflinePlayer(args[0]);

            if (toIgnore.hasPlayedBefore()) {
                boolean added = Ignores.toggleIgnore(player, toIgnore.getUniqueId());

                if (added) player.sendMessage(Prefixes.MPVP + ChatColor.GRAY + "Added " + ChatColor.WHITE + toIgnore.getName() + ChatColor.GRAY + " to ignore list");
                else player.sendMessage(Prefixes.MPVP + ChatColor.GRAY + "Removed " + ChatColor.WHITE + toIgnore.getName() + ChatColor.GRAY + " from ignore list");
            } else {
                player.sendMessage(Prefixes.MPVP + ChatColor.WHITE + args[0] + ChatColor.GRAY + " never played here before");
            }
        }

        return true;
    }
}
