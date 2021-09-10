package meteordevelopment.meteorpvp.commands.kits;

import meteordevelopment.meteorpvp.chat.Msgs;
import meteordevelopment.meteorpvp.chat.Prefixes;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.kits.Kit;
import meteordevelopment.meteorpvp.kits.Kits;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeleteKit extends MyCommand {
    private static final List<String> LIST = new ArrayList<>();

    public DeleteKit() {
        super("deletekit", "Deletes a kit.", "/deletekit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

        boolean deleted;
        if (Utils.isAdmin(player)) {
            deleted = Kits.INSTANCE.remove(args[0]);
        }
        else {
            deleted = Kits.INSTANCE.remove(player, args[0]);
        }

        if (deleted) {
            player.sendMessage(Prefixes.KITS + Msgs.deletedKit(args[0]));
        }
        else {
            player.sendMessage(Prefixes.KITS + Msgs.kitDoesntExist(args[0]));
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args, Location location) throws IllegalArgumentException {
        LIST.clear();
        if (sender instanceof Player) {
            for (Kit kit : Kits.INSTANCE.getKits((Player) sender)) LIST.add(kit.name);
        }
        return LIST;
    }
}
