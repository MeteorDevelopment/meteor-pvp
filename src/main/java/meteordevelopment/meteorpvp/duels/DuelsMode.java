package meteordevelopment.meteorpvp.duels;

import meteordevelopment.meteorpvp.arenas.Arena;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class DuelsMode {
    public final World world;
    public final TerrainType terrainType;

    private final List<Arena> arenas = new ArrayList<>();

    public DuelsMode(World world, TerrainType terrainType, int distance) {
        this.world = world;
        this.terrainType = terrainType;

        add(new Arena(world, distance, distance));
        add(new Arena(world, distance, -distance));
        add(new Arena(world, -distance, distance));
        add(new Arena(world, -distance, -distance));
    }

    private void add(Arena duel) {
        arenas.add(duel);
    }

    public boolean anyArenasAvailable() {
        return getAvailable() != null;
    }

    public int getAvailableCount() {
        int count = 0;

        for (Arena arena : arenas) {
            if (arena.isAvailable()) count++;
        }

        return count;
    }

    public Arena getAvailable() {
        for (Arena arena : arenas) {
            if (arena.isAvailable()) return arena;
        }

        return null;
    }

    public boolean isIn(Location location) {
        for (Arena arena : arenas) {
            if (arena.isIn(location)) return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return world == Utils.NETHER ? "nether" : "overworld" + (terrainType == TerrainType.Flat ? " flat" : "");
    }

    public enum TerrainType {
        Normal,
        Flat
    }
}
