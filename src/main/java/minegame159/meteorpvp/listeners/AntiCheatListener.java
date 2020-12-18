package minegame159.meteorpvp.listeners;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import it.unimi.dsi.fastutil.objects.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class AntiCheatListener implements Listener {
    public static AntiCheatListener INSTANCE;

    public final Object2IntMap<Player> ignoreTicks = new Object2IntOpenHashMap<>();
    private final Object2ObjectMap<Player, Location> lastValidSpeedPositions = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectMap<Player, Location> lastOnGroundPositions = new Object2ObjectOpenHashMap<>();
    private final Object2IntMap<Player> inAirTicks = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Player> inAirTicks2 = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Player> fallingTicks = new Object2IntOpenHashMap<>();
    private final Object2DoubleMap<Player> lastVelocityY = new Object2DoubleOpenHashMap<>();
    private final Object2IntMap<Player> highYVelocityTicks = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Player> highButLessYVelocityTicks = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Player> timesStoppedFalling = new Object2IntOpenHashMap<>();
    private final Object2BooleanMap<Player> lastWasFalling = new Object2BooleanOpenHashMap<>();
    private final Object2ObjectMap<Player, Location> lastValidPhasePositions = new Object2ObjectOpenHashMap<>();
    private final Object2BooleanMap<Player> lastWasInsideVehicle = new Object2BooleanOpenHashMap<>();
    private final Object2ObjectMap<Player, Location> lastPositions = new Object2ObjectOpenHashMap<>();
    private final Object2BooleanMap<Player> ignoreSpeedNextTick = new Object2BooleanOpenHashMap<>();

    private final Object2ObjectMap<Player, int[]> lastSecondPerTickPlayerMoveTimes = new Object2ObjectOpenHashMap<>();
    private int lastSecondTickI = 0;

    public AntiCheatListener() {
        INSTANCE = this;
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Location from = event.getFrom();
        Location to = event.getTo();

        int[] lastSecondTicks = lastSecondPerTickPlayerMoveTimes.get(player);
        if (lastSecondTicks == null) {
            lastSecondTicks = new int[20];
            lastSecondPerTickPlayerMoveTimes.put(player, lastSecondTicks);
        }

        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            lastSecondTicks[lastSecondTickI] = lastSecondTicks[lastSecondTickI] + 1;
        }

        if (event.isCancelled() || player.isDead() || player.getGameMode() != GameMode.SURVIVAL) return;

        int totalMoveEventsLastSecond = 0;
        for (int x : lastSecondTicks) totalMoveEventsLastSecond += x;
        if (totalMoveEventsLastSecond > 35.0 * (20.0 / Bukkit.getTPS()[0])) event.setCancelled(true);

        if (!ignore(player) && !ignoreSpeedNextTick.getBoolean(player)) {
            double y = 0;
            double velY = to.getY() - from.getY();
            if (velY > 2) y = Math.pow(velY, 2);
            double speed = Math.sqrt(Math.pow(to.getX() - from.getX(), 2) + y + Math.pow(to.getZ() - from.getZ(), 2));

            if (velY > 0.6) {
                int ticks = highYVelocityTicks.getInt(player);
                highYVelocityTicks.put(player, ticks + 1);
            } else {
                highYVelocityTicks.removeInt(player);
            }

            if (velY > 0.3 && !player.isGliding()) {
                int ticks = highButLessYVelocityTicks.getInt(player);
                highButLessYVelocityTicks.put(player, ticks + 1);
            } else {
                highButLessYVelocityTicks.removeInt(player);
            }

            double maxSpeed;
            if (player.isGliding()) maxSpeed = getElytraSpeedLimit();
            else maxSpeed = getNormalSpeedLimit();

            if (speed > maxSpeed) {
                Location pos = lastValidSpeedPositions.get(player);
                if (pos != null) {
                    event.setTo(pos);
                    to = pos;
                }
                if (player.isInsideVehicle()) player.leaveVehicle();
            } else {
                lastValidSpeedPositions.put(player, event.getTo());
            }
        } else {
            highYVelocityTicks.removeInt(player);
            highButLessYVelocityTicks.removeInt(player);
            lastValidSpeedPositions.put(player, event.getTo());
        }

        lastVelocityY.put(player, to.getY() - from.getY());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        lastValidSpeedPositions.remove(player);
        ignoreTicks.removeInt(player);
        lastOnGroundPositions.remove(player);
        inAirTicks.removeInt(player);
        inAirTicks2.removeInt(player);
        fallingTicks.removeInt(player);
        lastVelocityY.removeDouble(player);
        highYVelocityTicks.removeInt(player);
        highButLessYVelocityTicks.removeInt(player);
        timesStoppedFalling.removeInt(player);
        lastWasFalling.removeBoolean(player);
        //lastValidPhasePositions.remove(player);
        lastWasInsideVehicle.removeBoolean(player);
        lastSecondPerTickPlayerMoveTimes.remove(player);
        lastPositions.remove(player);
        ignoreSpeedNextTick.removeBoolean(player);
    }

    @EventHandler
    private void onPlayerVelocity(PlayerVelocityEvent event) {
        ignoreTicks.put(event.getPlayer(), (int) Math.round(Math.max(event.getVelocity().length() * 25, 60)));
    }

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event) {
        lastSecondTickI++;
        if (lastSecondTickI > 19) lastSecondTickI = 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            int[] ticks = lastSecondPerTickPlayerMoveTimes.get(player);
            if (ticks != null) ticks[lastSecondTickI] = 0;

            if (player.isDead()) continue;

            boolean isInsideVehicle = player.isInsideVehicle();
            if (!lastWasInsideVehicle.getBoolean(player) && isInsideVehicle) ignoreTicks.put(player, 6);
            lastWasInsideVehicle.put(player, isInsideVehicle);

            boolean onGround = isOnGround(player);
            if (onGround) {
                lastOnGroundPositions.put(player, player.getLocation());
                timesStoppedFalling.removeInt(player);
            }

            ignoreSpeedNextTick.removeBoolean(player);
            if (ignore(player)) continue;

            if (!player.isGliding()) {
                // Flight
                double velY = lastVelocityY.getOrDefault(player, 0.0);
                int yVelTicks = highYVelocityTicks.getInt(player);
                int lessYVelTicks = highButLessYVelocityTicks.getInt(player);

                if (lastWasFalling.getBoolean(player) && velY >= 0) {
                    int timesStoppedFalling = this.timesStoppedFalling.getInt(player) + 1;
                    this.timesStoppedFalling.put(player, timesStoppedFalling);
                }

                lastWasFalling.put(player, velY < 0);

                int ticksFalling = 0;

                if (!onGround && velY < 0) {
                    ticksFalling = fallingTicks.getInt(player) + 1;
                    fallingTicks.put(player, ticksFalling);
                } else {
                    fallingTicks.removeInt(player);
                }

                boolean inAir = !onGround && velY >= 0 && (velY <= 0.25 || yVelTicks > 4 || lessYVelTicks > 8);
                int ticksInAir = 0;

                if (inAir) {
                    ticksInAir = inAirTicks.getInt(player) + 1;
                    inAirTicks.put(player, ticksInAir);
                } else {
                    inAirTicks.removeInt(player);
                }

                boolean inAir2 = !onGround;
                if (!onGround && velY < 0 && ticksFalling > 4) inAir2 = false;
                int ticksInAir2 = 0;

                if (inAir2) {
                    ticksInAir2 = inAirTicks2.getInt(player) + 1;
                    inAirTicks2.put(player, ticksInAir2);
                } else {
                    inAirTicks2.removeInt(player);
                }

                if (player.getGameMode() == GameMode.SURVIVAL && ((ticksInAir >= 10) || (ticksInAir2 > 14) || (inAir && timesStoppedFalling.getInt(player) > 1))) {
                    Location pos = lastOnGroundPositions.get(player);
                    if (pos != null) {
                        player.teleport(pos);
                        ignoreSpeedNextTick.put(player, true);
                    }
                }
            }

            // Tick Speed
            /*Location pos = player.getLocation();

            if (player.getGameMode() == GameMode.SURVIVAL && !ignoreSpeedNextTick.getBoolean(player)) {
                Location lastPos = lastPositions.getOrDefault(player, pos);
                double speed = Math.sqrt(Math.pow(pos.getX() - lastPos.getX(), 2) + Math.pow(pos.getZ() - lastPos.getZ(), 2));

                double maxSpeed = 2;
                if (player.isGliding()) maxSpeed = 5;

                if (speed > maxSpeed * (20.0 / Bukkit.getTPS()[0])) {
                    pos = lastPos;
                    player.teleport(pos);
                }
            }

            lastPositions.put(player, pos);*/

            // Phase
            if (isInBlock(player)) {
                Location pos = lastValidPhasePositions.get(player);
                if (pos != null) player.teleport(pos);
            } else {
                lastValidPhasePositions.put(player, player.getLocation());
            }
        }
    }

    private boolean isInBlock(Player player) {
        return player.getLocation().getBlock().getType().isOccluding();
    }

    private boolean isOnGround(Player player) {
        Location pos = player.getLocation();

        if (check(pos, 0, 0)) return true;

        double xa = pos.getX() - pos.getBlockX();
        double za = pos.getZ() - pos.getBlockZ();

        if (xa >= 0 && xa <= 0.3 && check(pos, -1, 0)) return true;
        if (xa >= 0.7 && check(pos, 1, 0)) return true;
        if (za >= 0 && za <= 0.3 && check(pos, 0, -1)) return true;
        if (za >= 0.7 && check(pos, 0, 1)) return true;

        if (xa >= 0 && xa <= 0.3 && za >= 0 && za <= 0.3 && check(pos, -1, -1)) return true;
        if (xa >= 0 && xa <= 0.3 && za >= 0.7 && check(pos, -1, 1)) return true;
        if (xa >= 0.7 && za >= 0 && za <= 0.3 && check(pos, 1, -1)) return true;
        return xa >= 0.7 && za >= 0.7 && check(pos, 1, 1);
    }

    private boolean check(Location pos, int x, int z) {
        for (int y = 0; y >= -1; y--) {
            Block block = pos.add(x, y, z).getBlock();

            if (!block.isPassable() || isClimbable(block) || block.isLiquid()) return true;
            pos.subtract(x, y, z);
        }

        return false;
    }

    private boolean isClimbable(Block block) {
        Material m = block.getType();
        return m == Material.LADDER || m == Material.VINE || m == Material.TWISTING_VINES_PLANT || m == Material.WEEPING_VINES_PLANT;
    }

    private boolean ignore(Player player) {
        if (ignoreTicks.containsKey(player)) {
            int ticksIgnore = ignoreTicks.getInt(player);

            if (ticksIgnore <= 0) {
                ignoreTicks.removeInt(player);
            } else {
                ignoreTicks.put(player, ticksIgnore - 1);
                return true;
            }
        }

        return false;
    }

    private double getNormalSpeedLimit() {
        if (Bukkit.getTPS()[0] < 18) return 0.65; // 12 BPS
        return 0.85; // 16 BPS
    }

    private double getElytraSpeedLimit() {
        if (Bukkit.getTPS()[0] < 18) return 0.85 * 1.62; // 26 BPS
        return 0.85 * 2.2; // 36 BPS
    }
}
