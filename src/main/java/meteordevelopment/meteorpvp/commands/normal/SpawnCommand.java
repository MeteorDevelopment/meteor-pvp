package meteordevelopment.meteorpvp.commands.normal;

import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.utils.Msgs;
import meteordevelopment.meteorpvp.utils.Prefixes;
import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends MyCommand {
    public SpawnCommand() {
        super("spawn", "Teleports you to the spawn.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        if (Regions.isInAnyPvp(player)) {
            sender.sendMessage(Prefixes.ARENA + Msgs.cantUseThisInPvp());
            return true;
        }

        player.teleport(Utils.NETHER.getSpawnLocation().add(0.5, 0, 0.5));

        return true;
    }
}
