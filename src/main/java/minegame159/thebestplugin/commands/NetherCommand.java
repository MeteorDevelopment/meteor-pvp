package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NetherCommand extends MyCommand {
    public NetherCommand() {
        super("nether", "Teleports you to nether.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).teleport(TheBestPlugin.NETHER_LOCATION);
        return true;
    }
}
