package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.ArenaClearer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearArenasCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArenaClearer.clear();
        return true;
    }
}
