package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ToggleKitCreatorCommand extends MyCommand {
    public ToggleKitCreatorCommand() {
        super("togglekitcreator", "Toggles kit creator.", null, Perms.TOGGLE_KIT_CREATOR);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        TheBestPlugin.KIT_CREATOR_ENABLED = !TheBestPlugin.KIT_CREATOR_ENABLED;
        sender.sendMessage(Prefixes.KITS + "Kit creator is now " + ChatColor.GRAY + (TheBestPlugin.KIT_CREATOR_ENABLED ? "on" : "off") + ChatColor.WHITE + ".");
        return true;
    }
}
