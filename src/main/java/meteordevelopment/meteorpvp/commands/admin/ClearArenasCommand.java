package meteordevelopment.meteorpvp.commands.admin;

import meteordevelopment.meteorpvp.arenas.ArenaClearer;
import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.commands.MyCommand;
import org.bukkit.command.CommandSender;

public class ClearArenasCommand extends MyCommand {
    public ClearArenasCommand() {
        super("cleararenas", "Clears arenas.", Perms.MODERATOR);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        ArenaClearer.clear(true);
        return true;
    }
}
