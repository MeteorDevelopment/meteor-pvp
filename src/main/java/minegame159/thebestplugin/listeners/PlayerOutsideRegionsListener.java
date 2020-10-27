package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import minegame159.thebestplugin.Perms;
import minegame159.thebestplugin.duels.DuelArena;
import minegame159.thebestplugin.duels.Duels;
import minegame159.thebestplugin.utils.Regions;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerOutsideRegionsListener implements Listener {
    private final Map<Player, Location> lastValidPositions = new HashMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastValidPositions.remove(event.getPlayer());
    }

    @EventHandler
    private void onTick(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.getGameMode() != GameMode.SURVIVAL || player.hasPermission(Perms.ALLOW_OUTSIDE)) continue;

            World world = player.getWorld();
            DuelArena duel = Duels.INSTANCE.getDuel(player);

            if ((duel != null && !duel.isIn(player)) || (world == Utils.OVERWORLD && !Regions.isInAnyOW(player)) || (world == Utils.NETHER && !Regions.isInAnyNether(player))) {
                Location pos = lastValidPositions.get(player);
                if (pos == null) pos = (world == Utils.OVERWORLD ? Utils.OVERWORLD : Utils.NETHER).getSpawnLocation().add(0.5, 0, 0.5);
                player.teleport(pos);
            } else {
                lastValidPositions.put(player, player.getLocation());
            }
        }
    }

}
