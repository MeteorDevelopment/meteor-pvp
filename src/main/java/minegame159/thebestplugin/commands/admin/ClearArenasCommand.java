package minegame159.thebestplugin.commands.admin;

import minegame159.thebestplugin.ArenaClearer;
import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.commands.MyCommand;
import org.bukkit.command.CommandSender;

public class ClearArenasCommand extends MyCommand {
    public ClearArenasCommand() {
        super("cleararenas", "Clears arenas.", null, Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {


        ArenaClearer.clear();
        return true;
    }
}
