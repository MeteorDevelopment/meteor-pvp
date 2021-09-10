package meteordevelopment.meteorpvp.listeners;

import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(Utils.NETHER.getSpawnLocation().add(0.5, 0, 0.5));
        event.getPlayer().spigot().respawn();

        event.setJoinMessage(ChatColor.GREEN + event.getPlayer().getName() + ChatColor.GRAY + " joined");
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        if (Regions.isInAnyPvp(event.getPlayer(), false)) {
            event.getPlayer().setHealth(0);
        }

        event.setQuitMessage(ChatColor.RED + event.getPlayer().getName() + ChatColor.GRAY + " left");
    }
}
