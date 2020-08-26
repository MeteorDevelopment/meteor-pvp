package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kits;
import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleKitCreatorCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TheBestPlugin.KIT_CREATOR_ENABLED = !TheBestPlugin.KIT_CREATOR_ENABLED;
        sender.sendMessage(Kits.MSG_PREFIX + "Kit creator is now " + ChatColor.GRAY + (TheBestPlugin.KIT_CREATOR_ENABLED ? "on" : "off") + ChatColor.WHITE + ".");
        return true;
    }
}
