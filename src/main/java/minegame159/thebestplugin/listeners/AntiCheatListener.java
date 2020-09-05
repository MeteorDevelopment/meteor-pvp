package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.NumberConversions;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatListener implements Listener {
    private final Map<Player, Location> lastValidPositions = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastValidPositions.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        lastValidPositions.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        lastValidPositions.remove(event.getEntity());
    }

    @EventHandler
    public void onTick(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead()) continue;

            Location pos = player.getLocation();
            Location lastPos = lastValidPositions.get(player);
            if (lastPos == null) lastPos = pos;

            double distance = NumberConversions.square(pos.getX() - lastPos.getX()) + NumberConversions.square(pos.getZ() - lastPos.getZ());

            if (distance > 0.8 && player.getGameMode() == GameMode.SURVIVAL) {
                player.teleport(lastPos);
            } else {
                lastValidPositions.put(player, pos);
            }
        }
    }
}
