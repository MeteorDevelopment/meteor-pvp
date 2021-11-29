package meteordevelopment.meteorpvp.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.duels.Duel;
import meteordevelopment.meteorpvp.duels.Duels;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OutsideRegionsListener implements Listener {
    private final Map<UUID, Location> lastValidPositions = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastValidPositions.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    private void onTick(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.isOp() || player.isWhitelisted()) continue;

            boolean outside = false;

            Duel duel = Duels.INSTANCE.get(player);
            if (duel != null && !duel.isIn(player.getLocation())) {
                outside = true;
            }
            else if (!Regions.isInAny(player.getWorld(), player)) {
                outside = true;
            }

            if (outside) {
                Location pos = lastValidPositions.get(player.getUniqueId());
                if (pos == null) {
                    pos = player.getWorld().getSpawnLocation().add(0.5, 0, 0.5);
                }

                player.teleport(pos);
            }
            else {
                lastValidPositions.put(player.getUniqueId(), player.getLocation());
            }
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().isOp() && event.canBuild() && !Regions.isInAnyBuildable(event.getBlock().getLocation()))
            event.setBuild(false);
    }
}
