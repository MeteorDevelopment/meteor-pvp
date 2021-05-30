package minegame159.meteorpvp.commands.normal;

import minegame159.meteorpvp.commands.MyCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCommand extends MyCommand {
    public SuicideCommand() {
        super("suicide", "Kills you.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setHealth(0);
        return true;
    }
}
