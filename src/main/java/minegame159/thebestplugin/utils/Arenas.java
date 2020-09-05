package minegame159.thebestplugin.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import minegame159.thebestplugin.duels.Duels;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Arenas {
    public static Region OVERWORLD;

    public static void onEnable() {
        OVERWORLD = new CuboidRegion(BukkitAdapter.adapt(Bukkit.getWorld("world")), BlockVector3.at(255, 90, 255), BlockVector3.at(-255, 0, -255));
    }

    public static boolean isInAnyPvp(Player player) {
        if (Utils.isIn(OVERWORLD, player)) return true;

        return Duels.INSTANCE.getDuel(player) != null;
    }
}
