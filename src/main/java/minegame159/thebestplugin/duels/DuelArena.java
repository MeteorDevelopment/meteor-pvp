package minegame159.thebestplugin.duels;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.entity.EntityAddToWorldEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import minegame159.thebestplugin.TheBestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DuelArena implements Listener {
    public static final int SIZE = 6 * 16;

    private final Region region;
    private final Region wall1, wall2, wall3, wall4;

    private boolean wallsGenerated = false;
    private boolean preparing = false;

    private final Location player1Location;
    private final Location player2Location;

    public Player player1, player2;
    private boolean started, starting;
    private int startTimer;

    public DuelArena(int x, int z) {
        World world = FaweAPI.getWorld("world");
        region = new CuboidRegion(world, BlockVector3.at(x, 0, z), BlockVector3.at(x + SIZE - 1, 255, z + SIZE - 1));
        wall1 = new CuboidRegion(world, BlockVector3.at(x, 0, z - 1), BlockVector3.at(x + SIZE - 1, 100, z - 1));
        wall2 = new CuboidRegion(world, BlockVector3.at(x + SIZE, 0, z), BlockVector3.at(x + SIZE, 100, z + SIZE - 1));
        wall3 = new CuboidRegion(world, BlockVector3.at(x + SIZE - 1, 0, z + SIZE), BlockVector3.at(x, 100, z + SIZE));
        wall4 = new CuboidRegion(world, BlockVector3.at(x - 1, 0, z), BlockVector3.at(x - 1, 100, z + SIZE - 1));

        player1Location = new Location(Bukkit.getWorld("world"), x + 15.5, 5, z + 15.5, -45, 0);
        player2Location = new Location(Bukkit.getWorld("world"), x + SIZE - 15.5, 5, z + SIZE - 15.5, 130, 0);

        Bukkit.getPluginManager().registerEvents(this, TheBestPlugin.INSTANCE);
    }

    public void start(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        TaskManager.IMP.async(() -> {
            try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld("world")).fastmode(true).build()) {
                preparing = true;
                editSession.replaceBlocks(region, new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK), BlockTypes.AIR);

                if (!wallsGenerated) {
                    editSession.setBlock(player1Location.getBlockX(), 4, player1Location.getBlockZ(), BlockTypes.BEDROCK);
                    editSession.setBlock(player2Location.getBlockX(), 4, player2Location.getBlockZ(), BlockTypes.BEDROCK);

                    editSession.setBlocks(wall1, BlockTypes.BEDROCK);
                    editSession.setBlocks(wall2, BlockTypes.BEDROCK);
                    editSession.setBlocks(wall3, BlockTypes.BEDROCK);
                    editSession.setBlocks(wall4, BlockTypes.BEDROCK);

                    wallsGenerated = true;
                }

                preparing = false;
                TaskManager.IMP.sync(() -> {
                    player1.teleport(player1Location);
                    player2.teleport(player2Location);

                    started = true;
                    starting = true;
                    startTimer = 20 * 5;

                    TheBestPlugin.STATS.playedDuels++;
                    TheBestPlugin.STATS.changed();

                    return null;
                });
            }
        });
    }

    public Player getOther(Player player) {
        if (player1 == player) return player2;
        return player1;
    }

    private void win(Player player) {
        player.getInventory().clear();

        player1.teleport(TheBestPlugin.SPAWN_LOCATION);
        player2.teleport(TheBestPlugin.SPAWN_LOCATION);

        if (starting) player.resetTitle();

        player1 = null;
        player2 = null;
        started = false;

        TheBestPlugin.DUELS.addEmptyArena(this);
    }

    private void setTitle(String text) {
        Title title = new Title(text, null, 4, 12, 4);
        player1.sendTitle(title);
        player2.sendTitle(title);
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

    private void winIfStarted(Player player) {
        if (started && (player == player1 || player == player2)) {
            win(getOther(player));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        winIfStarted(event.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        winIfStarted(event.getPlayer());
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
            Location pos = event.getEntity().getLocation();

            if (region.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ())) {
                event.getEntity().remove();
            }
        }
    }
}
