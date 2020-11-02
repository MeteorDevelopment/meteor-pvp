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
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class AntiCheatListener implements Listener {
    private final Map<Player, Integer> ignoreTicks = new HashMap<>();
    private final Map<Player, Location> lastValidSpeedPositions = new HashMap<>();
    private final Map<Player, Location> lastOnGroundPositions = new HashMap<>();
    private final Map<Player, Integer> inAirTicks = new HashMap<>();
    private final Map<Player, Double> lastVelocityY = new HashMap<>();
    private final Map<Player, Integer> highYVelocityTicks = new HashMap<>();
    private final Map<Player, Integer> highButLessYVelocityTicks = new HashMap<>();

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.isCancelled() || player.isDead() || player.getGameMode() != GameMode.SURVIVAL) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (!ignore(player)) {
            double y = 0;
            double velY = to.getY() - from.getY();
            if (velY > 2) y = Math.pow(velY, 2);
            double speed = Math.sqrt(Math.pow(to.getX() - from.getX(), 2) + y + Math.pow(to.getZ() - from.getZ(), 2));

            if (velY > 0.5) {
                int ticks = highYVelocityTicks.getOrDefault(player, 0);
                highYVelocityTicks.put(player, ticks + 1);
            } else {
                highYVelocityTicks.remove(player);
            }

            if (velY > 0.2) {
                int ticks = highButLessYVelocityTicks.getOrDefault(player, 0);
                highButLessYVelocityTicks.put(player, ticks + 1);
            } else {
                highButLessYVelocityTicks.remove(player);
            }

            if (speed > 1) {
                Location pos = lastValidSpeedPositions.get(player);
                if (pos != null) {
                    event.setTo(pos);
                    to = pos;
                }
            } else {
                lastValidSpeedPositions.put(player, event.getTo());
            }
        } else {
            highYVelocityTicks.remove(player);
            highButLessYVelocityTicks.remove(player);
            lastValidSpeedPositions.put(player, event.getTo());
        }

        lastVelocityY.put(player, to.getY() - from.getY());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        lastValidSpeedPositions.remove(player);
        ignoreTicks.remove(player);
        lastOnGroundPositions.remove(player);
        inAirTicks.remove(player);
        lastVelocityY.remove(player);
        highYVelocityTicks.remove(player);
        highButLessYVelocityTicks.remove(player);
    }

    @EventHandler
    private void onPlayerVelocity(PlayerVelocityEvent event) {
        ignoreTicks.put(event.getPlayer(), 16);
    }

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.getGameMode() != GameMode.SURVIVAL) continue;

            boolean onGround = isOnGround(player);
            if (onGround) {
                lastOnGroundPositions.put(player, player.getLocation());
            }

            if (ignore(player)) continue;

            ItemStack chestplate = player.getInventory().getChestplate();
            if (chestplate != null && chestplate.getType() == Material.ELYTRA) continue;

            double velY = lastVelocityY.getOrDefault(player, 0.0);
            int yVelTicks = highYVelocityTicks.getOrDefault(player, 0);
            int lessYVelTicks = highButLessYVelocityTicks.getOrDefault(player, 0);

            boolean inAir = !onGround && velY >= 0 && (velY <= 0.25 || yVelTicks > 4 || lessYVelTicks > 8);
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

    private boolean ignore(Player player) {
        Integer ticksIgnore = ignoreTicks.get(player);
        if (ticksIgnore != null) {
            if (ticksIgnore <= 0) {
                ignoreTicks.remove(player);
            } else {
                ignoreTicks.put(player, ticksIgnore - 1);
                return true;
            }
        }

        return false;
    }
}
