package meteordevelopment.meteorpvp.commands.normal;

import meteordevelopment.meteorpvp.chat.PrivateMsgs;
import meteordevelopment.meteorpvp.commands.MyCommand;
import meteordevelopment.meteorpvp.chat.Prefixes;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand extends MyCommand {
    private final StringBuilder sb = new StringBuilder();

    public ReplyCommand() {
        super("r", "Replies to the last message.", "/r <message>");
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player player)) return false;

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
