package minegame159.thebestplugin.duels;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Utils;
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

        this.player1Location = new Location(world, x + 15, 6, z + 15);
        this.player2Location = new Location(world, x + SIZE - 15, 6, z + SIZE - 15);

        Bukkit.getPluginManager().registerEvents(this, TheBestPlugin.INSTANCE);
    }

    public void start(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        mode.moveToUsed(this);
        Duels.INSTANCE.duels.put(player1, this);
        Duels.INSTANCE.duels.put(player2, this);

        preparing = true;
        wonBeforePrepare = false;

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

    public boolean isIn(Entity entity) {
        Location pos = entity.getLocation();
        return region.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
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

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity() == player1 || event.getEntity() == player2) {
            win(getOther(event.getEntity()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer() == player1 || event.getPlayer() == player2) {
            win(getOther(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (starting && (event.getPlayer() == player1 || event.getPlayer() == player2)) {
            Location from = event.getFrom();
            event.getTo().set(from.getX(), from.getY(), from.getZ());
        }
    }

    @EventHandler
    public void onEntityAddToWorld(EntityAddToWorldEvent event) {
        if (preparing) {
            if (isIn(event.getEntity())) {
                event.getEntity().remove();
            }
        }
    }

    private void win(Player player) {
        Utils.resetToSpawn(player1);
        Utils.resetToSpawn(player2);

        if (starting) {
            player1.resetTitle();
            player2.resetTitle();
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
