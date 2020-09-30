package minegame159.thebestplugin.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinMessageListener implements Listener {
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(String.format("%s%s %sjoined", ChatColor.GREEN, event.getPlayer().getName(), ChatColor.GRAY));
    }

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        event.setQuitMessage(String.format("%s%s %sleft", ChatColor.RED, event.getPlayer().getName(), ChatColor.GRAY));
    }
}
