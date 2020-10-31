package minegame159.thebestplugin.commands.admin;

import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.commands.MyCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand extends MyCommand {
    public FeedCommand() {
        super("feed", "Fills your hunger bar.", null, Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) {
            ((Player) sender).setFoodLevel(20);
            ((Player) sender).setSaturation(5);
        }
        return true;
    }
}
