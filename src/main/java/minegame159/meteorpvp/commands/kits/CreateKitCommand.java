package minegame159.meteorpvp.commands.kits;

import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.kits.Kit;
import minegame159.meteorpvp.kits.Kits;
import minegame159.meteorpvp.kits.MaxKits;
import minegame159.meteorpvp.utils.Msgs;
import minegame159.meteorpvp.utils.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCommand extends MyCommand {
    public CreateKitCommand() {
        super("createkit", "Creates a new private kit.", "/createkit <name>", null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (args.length != 1) return false;
        Player player = (Player) sender;

        MaxKits maxKits = MaxKits.get(player);

        if (Kits.INSTANCE.getKits(player).size() >= maxKits.count) {
            player.sendMessage(Prefixes.KITS + Msgs.maxKits(maxKits));
            return true;
        }

        if (Kits.INSTANCE.get(args[0]) != null) {
            player.sendMessage(Prefixes.KITS + Msgs.kitAlreadyExists(args[0]));
            return true;
        }

        Kits.INSTANCE.add(new Kit(args[0], player));
        player.sendMessage(Prefixes.KITS + Msgs.createdKit(args[0]));

        return true;
    }
}
