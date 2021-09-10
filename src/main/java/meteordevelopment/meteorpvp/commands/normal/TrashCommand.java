package meteordevelopment.meteorpvp.commands.normal;

import meteordevelopment.meteorpvp.commands.MyCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TrashCommand extends MyCommand {
    public TrashCommand() {
        super("trash", "Opens a trash can.");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

        player.openInventory(Bukkit.createInventory(player, 9 * 4, Component.text("Trash Can")));
        return true;
    }
}
