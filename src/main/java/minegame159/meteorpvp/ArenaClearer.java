package minegame159.meteorpvp;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.function.mask.SingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import minegame159.meteorpvp.utils.Prefixes;
import minegame159.meteorpvp.utils.Regions;
import minegame159.meteorpvp.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ArenaClearer {
    public static void onEnable() {
        Bukkit.getScheduler().runTaskTimer(MeteorPvp.INSTANCE, ArenaClearer::clear, 20 * 60 * 60 * 6, 20 * 60 * 60 * 6);
    }

    public static void clear() {
        forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas in 30 seconds."));

        Bukkit.getScheduler().runTaskLater(MeteorPvp.INSTANCE, () -> {
            forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas in 5 seconds."));

            Bukkit.getScheduler().runTaskLater(MeteorPvp.INSTANCE, () -> {
                clean("world", Regions.OW_PVP, Regions.OW_AC_BARRIER);
                clean("world_nether", Regions.NETHER_PVP, Regions.NETHER_AC_BARRIER);
            }, 20 * 5);
        }, 20 * 30);
    }

    private static void clean(String worldName, ProtectedRegion clearRegion, ProtectedRegion barrierRegion) {
        World world = Bukkit.getWorld(worldName);
        forEachPlayer(world, Utils::resetToSpawn);

        TaskManager.IMP.async(() -> {
            try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(worldName)).fastmode(true).build()) {
                barrierEnable(editSession, barrierRegion);

                editSession.replaceBlocks(
                        Regions.toWERegion(clearRegion),
                        new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK),
                        BlockTypes.AIR
                );

                barrierDisable(editSession, barrierRegion);
            }

            forEachPlayer(world, player -> player.sendMessage(Prefixes.ARENA + "Arena cleared."));
        });
    }

    private static void forEachPlayer(Consumer<Player> consumer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            consumer.accept(player);
        }
    }

    private static void forEachPlayer(World world, Consumer<Player> consumer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == world) consumer.accept(player);
        }
    }

    private static void barrierEnable(EditSession editSession, ProtectedRegion region) {
        editSession.replaceBlocks(
                Regions.toWERegion(region),
                new SingleBlockTypeMask(editSession, BlockTypes.AIR),
                BlockTypes.BARRIER
        );
    }

    private static void barrierDisable(EditSession editSession, ProtectedRegion region) {
        editSession.replaceBlocks(
                Regions.toWERegion(region),
                new SingleBlockTypeMask(editSession, BlockTypes.BARRIER),
                BlockTypes.AIR
        );
    }
}
