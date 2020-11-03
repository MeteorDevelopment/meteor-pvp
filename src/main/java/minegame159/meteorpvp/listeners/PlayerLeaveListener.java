package minegame159.meteorpvp.listeners;

import minegame159.meteorpvp.utils.Regions;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        if (Regions.isInAnyPvp(event.getPlayer(), false)) {
            event.getPlayer().setHealth(0);
        }
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().spigot().respawn();
    }
}
