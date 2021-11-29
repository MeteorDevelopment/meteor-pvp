package meteordevelopment.meteorpvp.arenas;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import meteordevelopment.meteorpvp.duels.Duel;
import org.bukkit.Location;
import org.bukkit.World;

public class Arena {
    public final World world;
    public final Region region;

    public Duel duel;

    public Arena(World world, int centerX, int centerY) {
        this.world = world;
        this.region = new CuboidRegion(BukkitAdapter.adapt(world), BlockVector3.at(centerX - 47, 0, centerY - 47), BlockVector3.at(centerX + 47, 100, centerY + 47));
    }

    public boolean isIn(Location location) {
        return region.contains(BukkitAdapter.asBlockVector(location));
    }

    public boolean isAvailable() {
        return duel == null;
    }
}

