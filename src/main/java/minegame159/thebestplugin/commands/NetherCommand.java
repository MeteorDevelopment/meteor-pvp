package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Regions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NetherCommand extends MyCommand {
    public NetherCommand() {
        super("nether", "Teleports you to nether.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            if (Regions.isInAnyPvp((Player) sender)) {
                sender.sendMessage(Prefixes.ARENA + Msgs.cantUseThisInPvp());
                return true;
            }

            ((Player) sender).teleport(TheBestPlugin.NETHER_LOCATION);
        }

        return true;
    }
}
