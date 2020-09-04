package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.ArenaClearer;
import org.bukkit.command.CommandSender;

public class ClearArenasCommand extends MyCommand {
    public ClearArenasCommand() {
        super("cleararenas", "Clears arenas.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        ArenaClearer.clear();
        return true;
    }
}
