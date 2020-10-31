package minegame159.thebestplugin.commands.admin;

import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.commands.MyCommand;
import minegame159.thebestplugin.utils.Msgs;
import minegame159.thebestplugin.utils.Prefixes;
import org.bukkit.command.CommandSender;

public class ToggleKitCreatorCommand extends MyCommand {
    public ToggleKitCreatorCommand() {
        super("togglekitcreator", "Toggles kit creator.", null, Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        TheBestPlugin.KIT_CREATOR_ENABLED = !TheBestPlugin.KIT_CREATOR_ENABLED;
        sender.sendMessage(Prefixes.KITS + Msgs.kitCreatorToggled(TheBestPlugin.KIT_CREATOR_ENABLED));
        return true;
    }
}
