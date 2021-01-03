package minegame159.meteorpvp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RedirectCommandListener implements Listener {
    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/w ")) {
            event.setMessage("/msg " + event.getMessage().substring(3));
        }
    }
}
