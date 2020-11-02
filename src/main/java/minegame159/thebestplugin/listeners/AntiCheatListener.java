package minegame159.thebestplugin.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatListener implements Listener {
    private final Map<Player, Location> lastValidSpeedPositions = new HashMap<>();
    private final Map<Player, Location> lastOnGroundPositions = new HashMap<>();
    private final Map<Player, Integer> inAirTicks = new HashMap<>();
    private final Map<Player, Double> lastVelocityY = new HashMap<>();

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        if (event.isCancelled() || event.getPlayer().isDead() || event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        double y = 0;
        if (to.getY() - from.getY() > 0) y = Math.pow(to.getY() - from.getY(), 2);
        double speed = Math.sqrt(Math.pow(to.getX() - from.getX(), 2) + y + Math.pow(to.getZ() - from.getZ(), 2));

        if (speed > 1) {
            Location pos = lastValidSpeedPositions.get(event.getPlayer());
            if (pos != null) {
                event.setTo(pos);
                to = pos;
            }
        } else {
            lastValidSpeedPositions.put(event.getPlayer(), event.getTo());
        }

        lastVelocityY.put(event.getPlayer(), to.getY() - from.getY());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        lastValidSpeedPositions.remove(event.getPlayer());
        lastOnGroundPositions.remove(event.getPlayer());
        inAirTicks.remove(event.getPlayer());
        lastVelocityY.remove(event.getPlayer());
    }

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.getGameMode() != GameMode.SURVIVAL) continue;

            boolean onGround = isOnGround(player);
            if (onGround) {
                lastOnGroundPositions.put(player, player.getLocation());
            }

            ItemStack chestplate = player.getInventory().getChestplate();
            if (chestplate != null && chestplate.getType() == Material.ELYTRA) continue;

            double velY = lastVelocityY.getOrDefault(player, 0.0);
            boolean inAir = !onGround && velY >= 0 && velY <= 0.25;
            int ticksInAir = 0;

            if (inAir) {
                ticksInAir = inAirTicks.getOrDefault(player, 0);
                inAirTicks.put(player, ticksInAir + 1);
            } else {
                inAirTicks.remove(player);
            }

            if (ticksInAir >= 10) {
                Location pos = lastOnGroundPositions.get(player);
                if (pos != null) player.teleport(pos);
            }
        }
    }

    private boolean isOnGround(Player player) {
        Location pos = player.getLocation().subtract(0, 1, 0);

        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                if (!pos.add(x, 0, z).getBlock().isPassable()) return true;
                pos.subtract(x, 0, z);
            }
        }

        return false;
    }
}
