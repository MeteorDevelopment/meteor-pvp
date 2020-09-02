package minegame159.thebestplugin.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;

public class Arenas {
    public static Region OVERWORLD;

    public static void onEnable() {
        OVERWORLD = new CuboidRegion(BukkitAdapter.adapt(Bukkit.getWorld("world")), BlockVector3.at(255, 90, 255), BlockVector3.at(-255, 0, -255));
    }
}
