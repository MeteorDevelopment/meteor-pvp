package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.commands.MyCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyReloadCommand extends MyCommand {
    public MyReloadCommand() {
        super("reload", "Reloads plugins.", Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kick(Component.text("Server reloading, please rejoin."));
        }

        Bukkit.reload();

        return true;
    }
}
