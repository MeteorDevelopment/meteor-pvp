package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.ArenaClearer;
import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import org.bukkit.command.CommandSender;

public class ClearArenasCommand extends MyCommand {
    public ClearArenasCommand() {
        super("cleararenas", "Clears arenas.", Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        ArenaClearer.clear();
        return true;
    }
}
