package minegame159.meteorpvp.commands.normal;

import minegame159.meteorpvp.PrivateMsgs;
import minegame159.meteorpvp.commands.MyCommand;
import minegame159.meteorpvp.utils.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends MyCommand {
    private final StringBuilder sb = new StringBuilder();

    public ReplyCommand() {
        super("r", "Replies to the last message.", "/r <message>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(args[i]);
        }

        if (!PrivateMsgs.reply(player, sb.toString())) {
            player.sendMessage(Prefixes.MPVP + "Nobody sent you a message or is offline");
        }
        sb.setLength(0);

        return true;
    }
}
