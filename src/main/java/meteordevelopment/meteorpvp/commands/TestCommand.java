package meteordevelopment.meteorpvp.commands;

import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand extends MyCommand {
    public TestCommand() {
        super("test", "Teleports to a test arena.", Perms.TEST);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.teleport(new Location(Utils.NETHER, 159159, 4, 159159));
        return true;
    }
}
