package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmsCommand extends MyCommand {
    public GmsCommand() {
        super("gms", "Sets your gamemode to survival.", null, Perms.CHANGE_GAMEMODE);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setGameMode(GameMode.SURVIVAL);
        return true;
    }
}
