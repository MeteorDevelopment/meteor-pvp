package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreatorCommand extends MyCommand {
    public KitCreatorCommand() {
        super("kitcreator", "Teleports you to kit creator.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).teleport(TheBestPlugin.KIT_CREATOR_LOCATION);
        return true;
    }
}
