package minegame159.thebestplugin;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import minegame159.thebestplugin.utils.Prefixes;
import minegame159.thebestplugin.utils.Regions;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ArenaClearer {
    public static void onEnable() {
        Bukkit.getScheduler().runTaskTimer(TheBestPlugin.INSTANCE, ArenaClearer::clear, 20 * 60 * 60 * 6, 20 * 60 * 60 * 6);
    }

    public static void clear() {
        forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arena in 30 seconds."));

        Bukkit.getScheduler().runTaskLater(TheBestPlugin.INSTANCE, () -> {
            forEachPlayer(player -> player.sendMessage(Prefixes.ARENA + "Clearing arena in 5 seconds."));

            Bukkit.getScheduler().runTaskLater(TheBestPlugin.INSTANCE, () -> {
                forEachPlayer(Utils::resetToSpawn);

                // Overworld
                TaskManager.IMP.async(() -> {
                    try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld("world")).fastmode(true).build()) {
                        editSession.replaceBlocks(
                                Regions.toWERegion(Regions.OW_PVP),
                                new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK),
                                BlockTypes.AIR
                        );
                    }

                    forEachPlayer(Utils.OVERWORLD, player -> player.sendMessage(Prefixes.ARENA + "Arena cleared."));
                });

                // Nether
                TaskManager.IMP.async(() -> {
                    try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld("world_nether")).fastmode(true).build()) {
                        editSession.replaceBlocks(
                                Regions.toWERegion(Regions.NETHER_PVP),
                                new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK),
                                BlockTypes.AIR
                        );
                    }

                    forEachPlayer(Utils.NETHER, player -> player.sendMessage(Prefixes.ARENA + "Arena cleared."));
                });
            }, 20 * 5);
        }, 20 * 30);
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
