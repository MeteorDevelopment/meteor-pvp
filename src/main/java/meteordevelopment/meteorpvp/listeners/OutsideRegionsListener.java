package meteordevelopment.meteorpvp.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import meteordevelopment.meteorpvp.arenas.Regions;
import meteordevelopment.meteorpvp.duels.Duel;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.utils.Perms;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
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
            if (player.isDead() || Utils.isAdmin(player) || player.hasPermission(Perms.ALLOWED_OUTSIDE)) continue;

            World world = player.getWorld();
            Duel duel = Duels.INSTANCE.get(player);

            boolean outside = false;
            if (duel != null) {
                if (!duel.isIn(player)) outside = true;
            } else {
                if (world == Utils.OVERWORLD && !Regions.isInAnyOW(player)) outside = true;
                else if (world == Utils.NETHER && !Regions.isInAnyNether(player)) outside = true;
            }

            if (outside) {
                Location pos = lastValidPositions.get(player.getUniqueId());
                if (pos == null)
                    pos = (world == Utils.OVERWORLD ? Utils.OVERWORLD : Utils.NETHER).getSpawnLocation().add(0.5, 0, 0.5);
                player.teleport(pos);
            } else {
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
