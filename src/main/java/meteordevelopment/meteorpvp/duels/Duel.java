package meteordevelopment.meteorpvp.duels;

import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.fastasyncworldedit.core.FaweAPI;
import com.fastasyncworldedit.core.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import meteordevelopment.meteorpvp.MeteorPvp;
import meteordevelopment.meteorpvp.arenas.Arena;
import meteordevelopment.meteorpvp.utils.Prefixes;
import meteordevelopment.meteorpvp.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Duel implements Listener {
    public final Arena arena;
    public final Player player1, player2;

    private Player winner = null;
    private Stage stage = null;
    private int timer = 0;

    public Duel(Arena arena, Player player1, Player player2) {
        this.arena = arena;
        this.player1 = player1;
        this.player2 = player2;

        Bukkit.getPluginManager().registerEvents(this, MeteorPvp.INSTANCE);
    }

    public void start() {
        if (stage != null) return;

        player1.sendMessage(Prefixes.DUELS + "Preparing arena.");
        player2.sendMessage(Prefixes.DUELS + "Preparing arena.");

        TaskManager.IMP.async(() -> {
            if (stage != null) return;

            try (EditSession editSession = FaweAPI.getEditSessionBuilder(BukkitAdapter.adapt(arena.world)).fastmode(true).build()) {
                editSession.replaceBlocks(arena.region, new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK), BlockTypes.AIR);
            }

            TaskManager.IMP.sync(() -> {
                int centerX = arena.region.getCenter().getBlockX();
                int centerZ = arena.region.getCenter().getBlockZ();
                int radius = arena.region.getWidth() / 2;

                player1.teleport(new Location(arena.world,  centerX + radius - 7, 5, centerZ + radius - 7));
                player2.teleport(new Location(arena.world, centerX - radius + 7, 5, centerZ - radius + 7));

                stage = Stage.Starting;
                timer = 20 * 5;

                return null;
            });
        });
    }

    @EventHandler
    public void onTick(ServerTickStartEvent event) {
        switch (stage) {
            case Starting -> {
                if (timer > 0) {
                    switch (timer) {
                        case 20 * 5 -> setTitle("5");
                        case 20 * 4 -> setTitle("4");
                        case 20 * 3 -> setTitle("3");
                        case 20 * 2 -> setTitle("2");
                        case 20 -> setTitle("1");
                    }

                    timer--;
                }

                stage = Stage.Fighting;
            }
            case Ending -> {
                if (timer >= 20 * 10) {
                    Utils.resetToSpawn(winner);

                    arena.duel = null;
                    ServerTickStartEvent.getHandlerList().unregister(this);

                    return;
                }

                timer++;
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!is(event.getPlayer()) || event.getPlayer().isOp() || stage == Stage.Fighting) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        if (deltaX != 0 || deltaZ != 0) {
            event.getTo().setX(from.getX());
            event.getTo().setZ(from.getZ());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (is(event.getEntity())) {
            end(getOther(event.getEntity()));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (is(event.getPlayer())) {
            end(getOther(event.getPlayer()));
        }
    }

    public void end(Player winner) {
        PlayerQuitEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        PlayerDeathEvent.getHandlerList().unregister(this);

        player1.resetTitle();
        player2.resetTitle();

        if (winner == null) {
            Utils.resetToSpawn(player1);
            Utils.resetToSpawn(player2);
        }
        else {
            Player loser = winner == player1 ? player2 : player1;

            Utils.resetToSpawn(loser);
            Bukkit.broadcastMessage(String.format("%s %s%s %swon duel against %s%s.", Prefixes.DUELS, ChatColor.GOLD, winner.getName(), ChatColor.GRAY, ChatColor.RED, loser.getName()));

            stage = Stage.Ending;
            this.winner = winner;
        }
    }

    private void setTitle(String text) {
        player1.showTitle(Title.title(Component.text(text), Component.text("")));
        player2.showTitle(Title.title(Component.text(text), Component.text("")));
    }

    public boolean is(Entity entity) {
        return player1 == entity || player2 == entity;
    }

    public boolean isIn(Location pos) {
        return arena.region.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public Player getOther(Player player) {
        if (player1 == player) return player2;
        return player1;
    }

    public enum Stage {
        Starting,
        Fighting,
        Ending
    }
}
