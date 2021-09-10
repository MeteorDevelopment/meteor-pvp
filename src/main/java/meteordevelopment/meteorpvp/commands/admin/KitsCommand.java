package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand extends MyCommand {
    public KitsCommand() {
        super("kits", "View kits of another person.", "/kits <username>", Perms.MODERATOR);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player invHolder) || args.length != 1) return false;

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        invHolder.openInventory(Kits.INSTANCE.guiPrivateKits(player.getUniqueId(), invHolder));
        return true;
    }
}
