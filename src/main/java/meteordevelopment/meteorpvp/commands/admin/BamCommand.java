package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.utils.Perms;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BamCommand extends MyCommand {
    public BamCommand() {
        super("bam", "Bans the UUID and IP of a player.", Perms.MODERATOR);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length != 1) return false;

        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return false;

        player.banPlayerFull("").save();


        return true;
    }
}
