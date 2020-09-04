package minegame159.thebestplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCanCommand extends MyCommand {
    public TrashCanCommand() {
        super("trasncan", "Opens a trash can.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        player.openInventory(Bukkit.createInventory(player, 9 * 4, "Trash Can"));
        return true;
    }
}
