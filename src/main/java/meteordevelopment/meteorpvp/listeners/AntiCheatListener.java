package meteordevelopment.meteorpvp.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import meteordevelopment.meteorpvp.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AntiCheatListener implements Listener {
    private final Object2ObjectMap<Player, Location> lastValidBurrowPositions = new Object2ObjectOpenHashMap<>();

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof EnderCrystal
                && event.getDamager() instanceof Player
                && event.getEntity().getTicksLived() <= Config.MIN_CRYSTAL_AGE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isDead() || player.getGameMode() == GameMode.SPECTATOR) continue;

            // Burrow
            if (isInBlock(player.getLocation())) {
                Location pos = lastValidBurrowPositions.get(player);
                if (pos != null) {
                    if (isInBlock(pos)) {
                        // Find new location if previous one is inside block too
                        Location loc = pos.clone();

                        for (int i = 0; i < 5; i++) {
                            loc.set(pos.getX(), pos.getY(), pos.getZ());
                            offset(loc, i);

                            if (isEmptyForPlayer(loc)) {
                                pos.set(loc.getX(), loc.getY(), loc.getZ());
                                break;
                            }
                        }
                    }

                    player.teleport(pos);
                }
            }
            else {
                lastValidBurrowPositions.put(player, player.getLocation());
            }
        }
    }

    private boolean isInBlock(Location loc) {
        Material block = loc.getBlock().getType();

        if (block == Material.SOUL_SAND || block == Material.ENDER_CHEST) return (loc.getY() - (int) loc.getY()) < 0.8;

        return block.isOccluding();
    }

    private void offset(Location loc, int i) {
        switch (i) {
            case 0: loc.add(0, 1, 0);
            case 1: loc.add(1, 0, 0);
            case 2: loc.add(-1, 0, 0);
            case 3: loc.add(0, 0, 1);
            case 4: loc.add(0, 0, -1);
        }
    }

    private boolean isEmptyForPlayer(Location loc) {
        return loc.getBlock().isEmpty() && loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY() + 1, loc.getBlockZ()).isEmpty();
    }
}
