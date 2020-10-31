package minegame159.thebestplugin.commands.normal;

import minegame159.thebestplugin.commands.MyCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends MyCommand {
    public SuicideCommand() {
        super("suicide", "Kills you.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setHealth(0);
        return true;
    }
}
