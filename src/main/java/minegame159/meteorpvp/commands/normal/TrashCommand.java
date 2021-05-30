package minegame159.meteorpvp.commands.normal;

import minegame159.meteorpvp.commands.MyCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCommand extends MyCommand {
    public TrashCommand() {
        super("trash", "Opens a trash can.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        player.openInventory(Bukkit.createInventory(player, 9 * 4, "Trash Can"));
        return true;
    }
}
