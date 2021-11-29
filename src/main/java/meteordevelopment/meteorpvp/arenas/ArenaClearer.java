package meteordevelopment.meteorpvp.arenas;

import com.fastasyncworldedit.core.FaweAPI;
import com.fastasyncworldedit.core.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import meteordevelopment.meteorpvp.MeteorPvp;
import meteordevelopment.meteorpvp.utils.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ArenaClearer {
    public static void onEnable() {
        Bukkit.getScheduler().runTaskTimer(MeteorPvp.INSTANCE, () -> clear(false), 20 * 60 * 60 * 2, 20 * 60 * 60 * 2);
    }

    public static void clear(boolean instant) {
        Runnable clean = () -> {
            clean("world", Regions.OW_PVP);
            clean("world_nether", Regions.NETHER_PVP);
        };

        if (!instant) {
            forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas in 30 seconds."));

            Bukkit.getScheduler().runTaskLater(MeteorPvp.INSTANCE, () -> {
                forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas in 5 seconds."));
                Bukkit.getScheduler().runTaskLater(MeteorPvp.INSTANCE, clean, 20 * 5);
            }, 20 * 30);
        }
        else {
            forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas."));
            clean.run();
        }
    }

    private static void clean(String worldName, ProtectedRegion clearRegion) {
        World world = Bukkit.getWorld(worldName);

        for (Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.ENDER_CRYSTAL) entity.remove();
        }

        TaskManager.IMP.async(() -> {
            try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(worldName)).fastmode(true).build()) {

                editSession.replaceBlocks(
                        Regions.toWERegion(clearRegion),
                        new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK),
                        BlockTypes.AIR
                );
            }

            forEachPlayer(player -> player.getWorld() == world, player -> player.sendMessage(Prefixes.ARENA + "Arena cleared."));
        });
    }

    private static void forEachPlayer(Consumer<Player> action) {
        forEachPlayer(player -> true, action);
    }

    private static void forEachPlayer(Predicate<Player> playerPredicate, Consumer<Player> action) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerPredicate.test(player)) action.accept(player);
        }
    }
}
