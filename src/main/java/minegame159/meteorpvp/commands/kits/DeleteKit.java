package minegame159.meteorpvp.commands.kits;

import minegame159.meteorpvp.Perms;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.kits.Kit;
import minegame159.meteorpvp.kits.Kits;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DeleteKit extends MyCommand {
    private static final List<String> LIST = new ArrayList<>();

    public DeleteKit() {
        super("deletekit", "Deletes a kit.", "/deletekit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        boolean deleted;
        if (player.hasPermission(Perms.ADMIN)) {
            deleted = Kits.INSTANCE.remove(args[0]);
        } else {
            deleted = Kits.INSTANCE.remove(player, args[0]);
        }

        if (deleted) {
            player.sendMessage(Prefixes.KITS + Msgs.deletedKit(args[0]));
        } else {
            player.sendMessage(Prefixes.KITS + Msgs.kitDoesntExist(args[0]));
        }

        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args, @Nullable Location location) throws IllegalArgumentException {
        LIST.clear();
        if (sender instanceof Player) {
            for (Kit kit : Kits.INSTANCE.getKits((Player) sender)) LIST.add(kit.name);
        }
        return LIST;
    }
}
