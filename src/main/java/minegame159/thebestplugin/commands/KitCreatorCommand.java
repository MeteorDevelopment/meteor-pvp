package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreatorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).teleport(TheBestPlugin.KIT_CREATOR_LOCATION);
        return true;
    }
}
