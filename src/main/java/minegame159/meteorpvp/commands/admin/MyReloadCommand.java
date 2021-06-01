package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.MeteorPvp;
import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.listeners.AntiCheatListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyReloadCommand extends MyCommand {
    public MyReloadCommand() {
        super("myreload", "Reloads plugins.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!sender.hasPermission(Perms.ADMIN)) return false;

        for (Player player : Bukkit.getOnlinePlayers()) player.kickPlayer("Server reloading, please rejoin.");
        AntiCheatListener.MIN_CRYSTAL_AGE = MeteorPvp.INSTANCE.getConfig().getInt("min_crystal_age");
        Bukkit.reload();
        return true;
    }
}
