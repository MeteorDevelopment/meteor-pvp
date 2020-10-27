package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Regions;
import org.bukkit.Bukkit;
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

            ((Player) sender).teleport(Bukkit.getWorld("world").getSpawnLocation().add(0.5, 0, 0.5));
        }

        return true;
    }
}
