package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.utils.Arenas;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreatorCommand extends MyCommand {
    public KitCreatorCommand() {
        super("kitcreator", "Teleports you to kit creator.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            if (Arenas.isInAnyPvp((Player) sender)) {
                sender.sendMessage(Prefixes.ARENA + Msgs.cantUseThisInPvp());
                return true;
            }

            ((Player) sender).teleport(TheBestPlugin.KIT_CREATOR_LOCATION);
        }

        return true;
    }
}
