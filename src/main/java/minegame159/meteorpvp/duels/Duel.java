package minegame159.meteorpvp.duels;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import minegame159.meteorpvp.MeteorPvp;
import minegame159.meteorpvp.utils.Prefixes;
import minegame159.meteorpvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Duel implements Listener {
    private static final int SIZE = 6 * 16;

    private final DuelsMode mode;
    private final World world;
    private final Region region;

    private final Location player1Location;
    private final Location player2Location;

    private Player player1;
    private Player player2;

    private boolean preparing, wonBeforePrepare;
    private boolean started, starting;
    private int startTimer;

    public Duel(DuelsMode mode, World world, int x, int z) {
        this.mode = mode;
        this.world = world;
        this.region = new CuboidRegion(BukkitAdapter.adapt(world), BlockVector3.at(x, 0, z), BlockVector3.at(x + SIZE, 119, z + SIZE));

        this.player1Location = new Location(world, x + 15, 5, z + 15);
        this.player2Location = new Location(world, x + SIZE - 15, 5, z + SIZE - 15);

        Bukkit.getPluginManager().registerEvents(this, MeteorPvp.INSTANCE);
    }

    public void start(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        mode.moveToUsed(this);
        Duels.INSTANCE.duels.put(player1, this);
        Duels.INSTANCE.duels.put(player2, this);

        preparing = true;
        wonBeforePrepare = false;

        player1.sendMessage(Prefixes.DUELS + "Preparing arena.");
        player2.sendMessage(Prefixes.DUELS + "Preparing arena.");

        TaskManager.IMP.async(() -> {
            try (EditSession editSession = FaweAPI.getEditSessionBuilder(BukkitAdapter.adapt(world)).fastmode(true).build()) {
                editSession.replaceBlocks(region, new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK), BlockTypes.AIR);
            }

            preparing = false;
            if (wonBeforePrepare) {
                wonBeforePrepare = false;
                return;
            }

            TaskManager.IMP.sync(() -> {
                player1.teleport(player1Location);
                player2.teleport(player2Location);

                started = true;
                starting = true;
                startTimer = 20 * 5;

                return null;
            });
        });
    }

    public boolean isIn(Location pos) {
        return region.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public boolean isIn(Entity entity) {
        return isIn(entity.getLocation());
    }

    public Player getOther(Player player) {
        if (player1 == player) return player2;
        return player1;
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        if (!started) return;

        if (startTimer > 0) {
            if (startTimer == 20 * 5) setTitle("5");
            else if (startTimer == 20 * 4) setTitle("4");
            else if (startTimer == 20 * 3) setTitle("3");
            else if (startTimer == 20 * 2) setTitle("2");
            else if (startTimer == 20) setTitle("1");

            startTimer--;
            return;
        }

        starting = false;
    }

    private boolean is(Entity entity) {
        return player1 == entity || player2 == entity;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (is(event.getEntity())) {
            win(getOther(event.getEntity()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (is(event.getPlayer())) {
            win(getOther(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (starting && is(event.getPlayer())) {
            Location from = event.getFrom();
            Location to = event.getTo();

            double deltaX = to.getX() - from.getX();
            double deltaZ = to.getZ() - from.getZ();

            if (deltaX != 0 || deltaZ != 0) {
                event.getTo().setX(from.getX());
                event.getTo().setZ(from.getZ());
            }
        }
    }

    private void win(Player player) {
        Player p1 = player1;
        Player p2 = player2;

        Utils.resetToSpawn(p1);
        Utils.resetToSpawn(p2);

        if (starting) {
            p1.resetTitle();
            p2.resetTitle();
        }

        if (preparing) wonBeforePrepare = true;

        mode.moveToAvailable(this);
        Duels.INSTANCE.duels.remove(player1, this);
        Duels.INSTANCE.duels.remove(player2, this);

        Bukkit.broadcastMessage(String.format("%s %s%s %swon duel against %s%s.", Prefixes.DUELS, ChatColor.GOLD, player.getName(), ChatColor.GRAY, ChatColor.RED, getOther(player).getName()));

        player1 = null;
        player2 = null;
        started = false;
    }

    private void setTitle(String text) {
        Title title = new Title(text, null, 4, 12, 4);
        player1.sendTitle(title);
        player2.sendTitle(title);
    }
}
