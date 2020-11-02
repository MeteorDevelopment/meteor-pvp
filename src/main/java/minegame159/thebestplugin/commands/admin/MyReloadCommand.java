package minegame159.thebestplugin.commands.admin;

import minegame159.thebestplugin.commands.MyCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyReloadCommand extends MyCommand {
    public MyReloadCommand() {
        super("myreload", "Reloads plugins.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!sender.isOp()) return false;

        for (Player player : Bukkit.getOnlinePlayers()) player.kickPlayer("Server reloading, please rejoin.");
        Bukkit.reload();
        return true;
    }
}
