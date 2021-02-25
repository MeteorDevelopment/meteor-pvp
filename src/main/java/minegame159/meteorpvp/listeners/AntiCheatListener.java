package minegame159.meteorpvp.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import it.unimi.dsi.fastutil.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AntiCheatListener implements Listener {
    private final Object2ObjectMap<Player, Location> lastValidBurrowPositions = new Object2ObjectOpenHashMap<>();

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead()) continue;

            // Burrow
            if (isInBlock(player)) {
                Location pos = lastValidBurrowPositions.get(player);
                if (pos != null) player.teleport(pos);
            }
            else {
                lastValidBurrowPositions.put(player, player.getLocation());
            }
        }
    }

    private boolean isInBlock(Player player) {
        return player.getLocation().getBlock().getType().isOccluding();
    }
}
