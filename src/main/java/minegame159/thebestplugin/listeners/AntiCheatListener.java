package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.NumberConversions;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatListener/* implements Listener */{
    private final Map<Player, Location> lastValidPositions = new HashMap<>();
    private final Map<Player, Integer> inactiveTicks = new HashMap<>();

    private boolean skipTeleportEvent = false;

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastValidPositions.remove(event.getPlayer());
        inactiveTicks.put(event.getPlayer(), 5);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (skipTeleportEvent) {
            skipTeleportEvent = false;
            return;
        }

        lastValidPositions.remove(event.getPlayer());
        inactiveTicks.put(event.getPlayer(), 5);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        lastValidPositions.remove(event.getEntity());
        inactiveTicks.put(event.getEntity(), 5);
    }

    @EventHandler
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        lastValidPositions.remove(event.getPlayer());
        inactiveTicks.put(event.getPlayer(), 5);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        lastValidPositions.remove(event.getPlayer());
        inactiveTicks.put(event.getPlayer(), 10);
    }

    @EventHandler
    public void onTick(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.getGameMode() != GameMode.SURVIVAL) continue;
            
            if (inactiveTicks.containsKey(player)) {
                int ticks = inactiveTicks.get(player);
                if (ticks <= 0) {
                    inactiveTicks.remove(player);
                } else {
                    ticks--;
                    inactiveTicks.put(player, ticks);
                    continue;
                }
            }

            Location pos = player.getLocation();
            Location lastPos = lastValidPositions.get(player);
            if (lastPos == null) lastPos = pos;

            Location teleportPos = null;

            double horizontalDistance = NumberConversions.square(pos.getX() - lastPos.getX()) + NumberConversions.square(pos.getZ() - lastPos.getZ());

            // Speed check
            if (horizontalDistance > 6) {
                // Moving too fast
                teleportPos = lastPos;
                sendDebug(player, "Moving too fast, value %.3f", horizontalDistance);
            }

            if (teleportPos == null) {
                // Passed all checks
                lastValidPositions.put(player, pos);
            } else {
                // Didn't pass some checks
                skipTeleportEvent = true;
                player.teleport(teleportPos);
            }
        }
    }

    private void sendDebug(Player player, String format, Object... args) {
        player.sendMessage(String.format("%s[%sAC%s] %s", ChatColor.GRAY, ChatColor.RED, ChatColor.GRAY, ChatColor.WHITE) + String.format(format, args));
    }
}
