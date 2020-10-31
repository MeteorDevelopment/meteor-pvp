package minegame159.thebestplugin.commands.admin;

import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.commands.MyCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GmcCommand extends MyCommand {
    public GmcCommand() {
        super("gmc", "Sets your gamemode to creative.", null, Perms.CHANGE_GAMEMODE);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setGameMode(GameMode.CREATIVE);
        return true;
    }
}
