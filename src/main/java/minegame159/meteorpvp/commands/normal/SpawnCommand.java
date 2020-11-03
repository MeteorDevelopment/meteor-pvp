package minegame159.meteorpvp.commands.normal;

import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import minegame159.meteorpvp.utils.Regions;
import minegame159.meteorpvp.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends MyCommand {
    public SpawnCommand() {
        super("spawn", "Teleports you to spawn.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            if (Regions.isInAnyPvp((Player) sender)) {
                sender.sendMessage(Prefixes.ARENA + Msgs.cantUseThisInPvp());
                return true;
            }

            ((Player) sender).teleport(Utils.OVERWORLD.getSpawnLocation().add(0.5, 0, 0.5));
        }

        return true;
    }
}
