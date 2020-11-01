package minegame159.thebestplugin.duels;

import org.bukkit.World;

import java.util.ArrayDeque;
import java.util.Queue;

public class DuelsMode {
    public final String arenaName;

    private final Queue<Duel> available = new ArrayDeque<>();

    public DuelsMode(World world, String arenaName, int x1, int z1, int x2, int z2, int x3, int z3, int x4, int z4, int x5, int z5) {
        this.arenaName = arenaName;

        available.add(new Duel(this, world, x1, z1));
        available.add(new Duel(this, world, x2, z2));
        available.add(new Duel(this, world, x3, z3));
        available.add(new Duel(this, world, x4, z4));
        available.add(new Duel(this, world, x5, z5));
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
