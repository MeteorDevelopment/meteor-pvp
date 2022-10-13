package meteordevelopment.meteorpvp.listeners;

import meteordevelopment.meteorpvp.chat.Mutes;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class RedirectCommandListener implements Listener {
    @EventHandler
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        if (msg.startsWith("/minecraft:")) msg = "/" + msg.substring(11);

        if (msg.startsWith("/me ")) {
            event.getPlayer().sendMessage(String.format("%sYou cannot use this command.", ChatColor.RED));
            event.setCancelled(true);
            return;
        }

        if (isPrivateMessage(msg) && Mutes.isMuted(event.getPlayer())) {
            event.getPlayer().sendMessage(String.format("%sYou cannot talk whilst muted.", ChatColor.RED));
            event.setCancelled(true);
            return;
        }

        if (msg.startsWith("/w ")) {
            event.setMessage("/msg " + event.getMessage().substring(3));
        }
        else if (msg.startsWith("/tell ")) {
            event.setMessage("/msg " + event.getMessage().substring(6));
        }
    }

    private boolean isPrivateMessage(String command) {
        return command.startsWith("/w ") || command.startsWith("/msg ") || command.startsWith("/r ") || command.startsWith("/tell ");
    }
}
