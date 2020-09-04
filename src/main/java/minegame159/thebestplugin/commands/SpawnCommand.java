package minegame159.thebestplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand extends MyCommand {
    public SpawnCommand() {
        super("spawn", "Teleports you to spawn.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).teleport(Bukkit.getWorld("world").getSpawnLocation());
        return true;
    }
}
