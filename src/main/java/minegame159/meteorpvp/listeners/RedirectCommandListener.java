package minegame159.meteorpvp.listeners;

import minegame159.meteorpvp.Mutes;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RedirectCommandListener implements Listener {
    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        if (isPrivateMessage(event.getMessage()) && Mutes.isMuted(event.getPlayer())) {
            event.getPlayer().sendMessage(String.format("%sYou cannot talk whilst muted.", ChatColor.RED));
            event.setCancelled(true);
            return;
        }

        if (event.getMessage().startsWith("/w ")) {
            event.setMessage("/msg " + event.getMessage().substring(3));
        }
    }

    private boolean isPrivateMessage(String command) {
        return command.startsWith("/w ") || command.startsWith("/msg ") || command.startsWith("/r ") || command.startsWith("/tell ");
    }
}
