package minegame159.meteorpvp.commands.admin;

import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends MyCommand {
    public HealCommand() {
        super("heal", "Fully heals you.", Perms.ADMIN);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setHealth(((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        return true;
    }
}
