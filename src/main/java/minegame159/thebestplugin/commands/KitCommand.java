package minegame159.thebestplugin.commands;

import minegame159.thebestplugin.Kits;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class KitCommand extends MyCommand {
    private static final List<String> EMPTY = new ArrayList<>(0);

    public KitCommand() {
        super("kit", "Gives you a kit.", "/kit <name>", null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (sender instanceof Player) ((Player) sender).setHealth(((Player) sender).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) {
            return Kits.INSTANCE.getNames();
        }

        return EMPTY;
    }
}
