package minegame159.thebestplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelpCommandListener implements Listener {
    private final Pattern HELP_PATTERN = Pattern.compile("/help( \\d+)?", Pattern.CASE_INSENSITIVE);

    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/help")) {
            event.setCancelled(true);
            Matcher matcher = HELP_PATTERN.matcher(event.getMessage());

            if (matcher.matches()) {
                String cmd = "help thebestplugin";
                if (matcher.group(1) != null) cmd += matcher.group(1);
                event.getPlayer().performCommand(cmd);
            }
        }
    }

    @EventHandler
    private void onTabComplete(TabCompleteEvent event) {
        if (event.getBuffer().startsWith("/help")) {
            event.getCompletions().clear();
            event.getCompletions().add("1");
            event.getCompletions().add("2");
        }
    }
}
