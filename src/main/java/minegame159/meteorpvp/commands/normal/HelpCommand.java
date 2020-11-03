package minegame159.meteorpvp.commands.normal;

import minegame159.meteorpvp.commands.Commands;
import minegame159.meteorpvp.commands.MyCommand;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.ChatPaginator;

import java.util.Arrays;
import java.util.List;

public class HelpCommand extends MyCommand {
    private static final List<String> TAB = Arrays.asList("1", "2");

    public HelpCommand() {
        super("help", "This message.", null, null);
    }

    @Override
    protected boolean onCommand(CommandSender sender, String label, String[] args) {
        int pageNumber;
        int pageHeight;
        int pageWidth;

        if (args.length == 0) {
            pageNumber = 1;
        } else if (NumberUtils.isDigits(args[args.length - 1])) {
            try {
                pageNumber = NumberUtils.createInteger(args[args.length - 1]);
            } catch (NumberFormatException exception) {
                pageNumber = 1;
            }
            if (pageNumber <= 0) {
                pageNumber = 1;
            }
        } else {
            pageNumber = 1;
        }

        if (sender instanceof ConsoleCommandSender) {
            pageHeight = ChatPaginator.UNBOUNDED_PAGE_HEIGHT;
            pageWidth = ChatPaginator.UNBOUNDED_PAGE_WIDTH;
        } else {
            pageHeight = ChatPaginator.CLOSED_CHAT_PAGE_HEIGHT - 1;
            pageWidth = ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Commands.COMMANDS.size(); i++) {
            MyCommand command = Commands.COMMANDS.get(i);

            if (command.testPermissionSilent(sender)) {
                if (i > 0) sb.append("\n");
                sb.append(ChatColor.GOLD).append(command.getName()).append(": ").append(ChatColor.WHITE).append(command.getDescription());
            }
        }

        ChatPaginator.ChatPage page = ChatPaginator.paginate(sb.toString(), pageNumber, pageWidth, pageHeight);

        StringBuilder header = new StringBuilder();
        header.append(ChatColor.YELLOW).append("--------- ").append(ChatColor.WHITE).append("Help");
        if (page.getTotalPages() > 1) {
            header.append(" (");
            header.append(page.getPageNumber());
            header.append("/");
            header.append(page.getTotalPages());
            header.append(")");
        }
        header.append(" ").append(ChatColor.YELLOW);
        for (int i = header.length(); i < ChatPaginator.GUARANTEED_NO_WRAP_CHAT_PAGE_WIDTH; i++) {
            header.append("-");
        }

        sender.sendMessage(header.toString());
        sender.sendMessage(page.getLines());

        return true;
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        return TAB;
    }
}
