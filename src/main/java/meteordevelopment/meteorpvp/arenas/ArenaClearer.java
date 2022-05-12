package meteordevelopment.meteorpvp.arenas;

import com.fastasyncworldedit.core.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import meteordevelopment.meteorpvp.MeteorPvp;
import meteordevelopment.meteorpvp.chat.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ArenaClearer {
    public static void onEnable() {
        clear(true);
        Bukkit.getScheduler().runTaskTimer(MeteorPvp.INSTANCE, () -> clear(false), 20 * 60 * 60 * 6, 20 * 60 * 60 * 6);
    }

    public static void clear(boolean instant) {
        if (!instant) {
            forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas in 30 seconds."));

            Bukkit.getScheduler().runTaskLater(MeteorPvp.INSTANCE, () -> {
                forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas in 5 seconds."));

                Bukkit.getScheduler().runTaskLater(MeteorPvp.INSTANCE, () -> {
                    clean("world", Regions.OW_PVP);
                    clean("world_nether", Regions.NETHER_PVP);
                }, 20 * 5);
            }, 20 * 30);
        }
        else {
            forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arenas."));

            clean("world", Regions.OW_PVP);
            clean("world_nether", Regions.NETHER_PVP);
        }
    }

    private static void clean(String worldName, ProtectedRegion clearRegion) {
        World world = Bukkit.getWorld(worldName);

        for (Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.ENDER_CRYSTAL) entity.remove();
        }

        FaweAPI.getTaskManager().async(() -> {
            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(FaweAPI.getWorld(worldName)).fastMode(true).build()) {
                editSession.replaceBlocks(
                        Regions.toWERegion(clearRegion),
                        new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK),
                        BlockTypes.AIR
                );
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
}
