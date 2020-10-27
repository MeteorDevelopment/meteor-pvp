package minegame159.thebestplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MyReloadCommand extends MyCommand {
    public MyReloadCommand() {
        super("myreload", "Reloads plugins.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!sender.isOp()) return false;

        Bukkit.reload();
        return true;
    }
}
