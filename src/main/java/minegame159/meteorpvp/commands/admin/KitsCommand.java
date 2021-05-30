package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.kits.Kits;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCommand extends MyCommand {
    public KitsCommand() {
        super("kits", "View kits of another person.", "/kits <username>", Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player invHolder = (Player) sender;

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        invHolder.openInventory(Kits.INSTANCE.guiPrivateKits(player.getUniqueId(), invHolder));
        return true;
    }
}
