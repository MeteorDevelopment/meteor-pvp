package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Perms;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand extends MyCommand {
    public HealCommand() {
        super("heal", "Heals you.", null, Perms.HEAL);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setHealth(((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        return true;
    }
}
