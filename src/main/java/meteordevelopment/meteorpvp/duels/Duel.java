package meteordevelopment.meteorpvp.duels;

import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.fastasyncworldedit.core.FaweAPI;
import com.fastasyncworldedit.core.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.block.BlockTypes;
import meteordevelopment.meteorpvp.MeteorPvp;
import meteordevelopment.meteorpvp.chat.Prefixes;
import meteordevelopment.meteorpvp.utils.Utils;
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
import org.bukkit.event.player.PlayerRespawnEvent;

public class Duel implements Listener {
    private static final int SIZE = 6 * 16;

    private final DuelsMode mode;
    private final World world;
    private final Region region;
    private final int x, z;

    private Player player1, player2, winner;

    private boolean starting, fighting, finishing, finished;

    private int timer;

    public Duel(DuelsMode mode, World world, int x, int z) {
        this.mode = mode;
        this.world = world;
        this.x = x;
        this.z = z;
        this.region = new CuboidRegion(BukkitAdapter.adapt(world), BlockVector3.at(x, 0, z), BlockVector3.at(x + SIZE, 119, z + SIZE));

        Bukkit.getPluginManager().registerEvents(this, MeteorPvp.INSTANCE);
    }

    public void start(Player player1, Player player2) {
        if (finished) return;

        this.player1 = player1;
        this.player2 = player2;

        mode.makeUnavailable(this);
        Duels.INSTANCE.duels.put(player1, this);
        Duels.INSTANCE.duels.put(player2, this);

        player1.sendMessage(Prefixes.DUELS + "Preparing arena.");
        player2.sendMessage(Prefixes.DUELS + "Preparing arena.");

        TaskManager.IMP.async(() -> {
            if (finished) return;

            try (EditSession editSession = FaweAPI.getEditSessionBuilder(BukkitAdapter.adapt(world)).fastmode(true).build()) {
                editSession.replaceBlocks(region, new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK), BlockTypes.AIR);
            }

            TaskManager.IMP.sync(() -> {
                player1.teleport(new Location(world, x + 15, 5, z + 15));
                player2.teleport(new Location(world, x + SIZE - 15, 5, z + SIZE - 15));

                starting = true;
                timer = 20 * 5;

                return null;
            });
        });
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        if (starting) {
            if (timer > 0) {
                switch (timer) {
                    case 20 * 5 -> setTitle("5");
                    case 20 * 4 -> setTitle("4");
                    case 20 * 3 -> setTitle("3");
                    case 20 * 2 -> setTitle("2");
                    case 20 -> setTitle("1");
                }

                timer--;
                return;
            }

            timer = 0;
            starting = false;
            fighting = true;
        }

        if (finishing) {
            if (timer >= 20 * 5)  {
                Utils.resetToSpawn(winner);

                mode.makeAvailable(this);
                Duels.INSTANCE.duels.remove(player1, this);
                Duels.INSTANCE.duels.remove(player2, this);

                finishing = false;
                finished = true;

                return;
            }

            timer++;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (finished) return;

        if (is(event.getEntity())) {
            win(getOther(event.getEntity()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (finished) return;

        if (is(event.getPlayer())) {
            win(getOther(event.getPlayer()));
        }
    }

    private void win(Player winner) {
        if (finished) return;

        this.winner = winner;

        Bukkit.broadcastMessage(String.format("%s %s%s %swon duel against %s%s.", Prefixes.DUELS, ChatColor.GOLD, winner.getName(), ChatColor.GRAY, ChatColor.RED, getOther(winner).getName()));

        if (starting) {
            player1.resetTitle();
            player2.resetTitle();
        }
        
        finished = true;
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (finished && is(event.getPlayer()) && isIn(event.getPlayer()) && event.getPlayer() != winner) {
            Utils.resetToSpawn(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (fighting || finishing || finished || !is(event.getPlayer()) || Utils.isAdmin(event.getPlayer())) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        if (deltaX != 0 || deltaZ != 0) {
            event.getTo().setX(from.getX());
            event.getTo().setZ(from.getZ());
        }
    }

    private void setTitle(String text) {
        Title title = new Title(text, null, 4, 12, 4);
        player1.sendTitle(title);
        player2.sendTitle(title);
    }

    private boolean is(Entity entity) {
        return player1 == entity || player2 == entity;
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
}
