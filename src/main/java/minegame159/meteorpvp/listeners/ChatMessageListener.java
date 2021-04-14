package minegame159.meteorpvp.listeners;

import minegame159.meteorpvp.Ignores;
import minegame159.meteorpvp.Mutes;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatMessageListener implements Listener {
    private final Map<UUID, String> lastMsgs = new HashMap<>();

    @EventHandler
    private void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();

        if (Mutes.isMuted(sender)) {
            sender.sendMessage(String.format("%sYou cannot talk whilst muted.", ChatColor.RED));
            event.setCancelled(true);
            return;
        }

        String lastMsg = lastMsgs.get(sender.getUniqueId());
        if (lastMsg != null && lastMsg.equals(event.getMessage())) {
            event.setCancelled(true);
            return;
        }

        event.getRecipients().removeIf(player -> Ignores.hasReceiverIgnored(sender, player));

        lastMsgs.put(sender.getUniqueId(), event.getMessage());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        lastMsgs.remove(event.getPlayer().getUniqueId());
    }
}
