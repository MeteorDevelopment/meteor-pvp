package minegame159.thebestplugin;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.util.TaskManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.mask.InverseSingleBlockTypeMask;
import com.sk89q.worldedit.world.block.BlockTypes;
import minegame159.thebestplugin.utils.Arenas;
import minegame159.thebestplugin.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ArenaClearer {
    private static final String PREFIX = ChatColor.GREEN + "[ARENA]: " + ChatColor.WHITE;

    public static void onEnable() {
        Bukkit.getScheduler().runTaskTimer(TheBestPlugin.INSTANCE, ArenaClearer::clear, 20 * 60 * 60 * 6, 20 * 60 * 60 * 6);
    }

    public static void clear() {
        World world = Bukkit.getWorld("world");
        
        forEachPlayer(world, player -> player.sendMessage(PREFIX + "Clearing arena in 30 seconds."));

        Bukkit.getScheduler().runTaskLater(TheBestPlugin.INSTANCE, () -> {
            forEachPlayer(world, player -> player.sendMessage(PREFIX + "Clearing arena in 5 seconds."));

            Bukkit.getScheduler().runTaskLater(TheBestPlugin.INSTANCE, () -> {
                forEachPlayer(world, Utils::resetToSpawn);

                TaskManager.IMP.async(() -> {
                    try (EditSession editSession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld("world")).fastmode(true).build()) {
                        editSession.replaceBlocks(Arenas.OVERWORLD, new InverseSingleBlockTypeMask(editSession, BlockTypes.BEDROCK), BlockTypes.AIR);
                    }

                    forEachPlayer(world, player -> player.sendMessage(PREFIX + "Arena cleared."));
                });
            }, 20 * 5);
        }, 20 * 30);
    }

    private static void forEachPlayer(World world, Consumer<Player> consumer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getWorld() == world) {
                consumer.accept(player);
            }
        }
    }
}
