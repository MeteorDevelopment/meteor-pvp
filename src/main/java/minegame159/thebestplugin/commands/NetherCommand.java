package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class NetherCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) ((Player) sender).teleport(TheBestPlugin.NETHER_LOCATION);
        return true;
    }
}
