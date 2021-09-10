package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.Config;
import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.chat.Msgs;
import meteordevelopment.meteorpvp.chat.Prefixes;
import org.bukkit.command.CommandSender;

public class ToggleKitCreatorCommand extends MyCommand {
    public ToggleKitCreatorCommand() {
        super("togglekitcreator", "Toggles kit creator.", Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        sender.sendMessage(Prefixes.KITS + Msgs.kitCreatorToggled(Config.KIT_CREATOR_ENABLED = !Config.KIT_CREATOR_ENABLED));
        return true;
    }
}
