package meteordevelopment.meteorpvp.commands.kits;

import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.kits.Kit;
import meteordevelopment.meteorpvp.kits.Kits;
import meteordevelopment.meteorpvp.kits.MaxKits;
import meteordevelopment.meteorpvp.chat.Msgs;
import meteordevelopment.meteorpvp.chat.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateKitCommand extends MyCommand {
    public CreateKitCommand() {
        super("createkit", "Creates a new private kit.", "/createkit <name>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;
        if (args.length != 1) return false;

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
