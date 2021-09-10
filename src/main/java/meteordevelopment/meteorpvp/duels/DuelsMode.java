package meteordevelopment.meteorpvp.duels;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DuelsMode {
    public final String arenaName;

    private final List<Duel> all = new ArrayList<>();
    private final Queue<Duel> available = new ArrayDeque<>();

    public DuelsMode(World world, String arenaName, int x1, int z1, int x2, int z2, int x3, int z3, int x4, int z4, int x5, int z5) {
        this.arenaName = arenaName;

        add(new Duel(this, world, x1, z1));
        add(new Duel(this, world, x2, z2));
        add(new Duel(this, world, x3, z3));
        add(new Duel(this, world, x4, z4));
        add(new Duel(this, world, x5, z5));
    }

    private void add(Duel duel) {
        all.add(duel);
        available.add(duel);
    }

    public boolean isIn(Location pos) {
        for (Duel duel : all) {
            if (duel.isIn(pos)) return true;
        }

        return false;
    }

    public Duel get() {
        return available.poll();
    }

    public boolean isAvailable() {
        return !available.isEmpty();
    }

    public int availableCount() {
        return available.size();
    }

    void moveToAvailable(Duel duel) {
        available.add(duel);
    }

    void moveToUsed(Duel duel) {
        available.remove(duel);
    }
}
