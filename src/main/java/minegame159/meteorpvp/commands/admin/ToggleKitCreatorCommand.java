package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.MeteorPvp;
import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import org.bukkit.command.CommandSender;

public class ToggleKitCreatorCommand extends MyCommand {
    public ToggleKitCreatorCommand() {
        super("togglekitcreator", "Toggles kit creator.", null, Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        MeteorPvp.KIT_CREATOR_ENABLED = !MeteorPvp.KIT_CREATOR_ENABLED;
        sender.sendMessage(Prefixes.KITS + Msgs.kitCreatorToggled(MeteorPvp.KIT_CREATOR_ENABLED));
        return true;
    }
}
