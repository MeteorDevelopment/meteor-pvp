package meteordevelopment.meteorpvp.arenas;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import meteordevelopment.meteorpvp.duels.Duels;
import meteordevelopment.meteorpvp.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Regions {
    // Overworld
    public static ProtectedRegion OW_SPAWN;
    public static ProtectedRegion OW_PVP;

    // Nether
    public static ProtectedRegion KIT_CREATOR;
    public static ProtectedRegion NETHER_SPAWN;
    public static ProtectedRegion NETHER_PVP;

    public static void onEnable() {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        RegionManager OW = container.get(BukkitAdapter.adapt(Utils.OVERWORLD));
        OW_SPAWN = OW.getRegion("spawn");
        OW_PVP = OW.getRegion("pvp");

        RegionManager NETHER = container.get(BukkitAdapter.adapt(Utils.NETHER));
        KIT_CREATOR = OW.getRegion("kitcreator");
        NETHER_SPAWN = NETHER.getRegion("spawn");
        NETHER_PVP = NETHER.getRegion("pvp");
    }

    public static boolean isIn(ProtectedRegion region, Location pos) {
        return region.contains(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
    }

    public static boolean isIn(ProtectedRegion region, Entity entity) {
        return isIn(region, entity.getLocation());
    }

    public static boolean isInAnyPvp(Player player) {
        if (player.isOp()) return false;

        return isIn(NETHER_PVP, player) || isIn(OW_PVP, player) || Duels.INSTANCE.get(player) != null;
    }

    public static boolean isInAny(World world, Player player) {
        return world == Utils.OVERWORLD ? isInAnyOW(player) : isInAnyNether(player);
    }

    public static boolean isInAnyOW(Player player) {
        return isIn(OW_SPAWN, player) || isIn(OW_PVP, player) || isIn(KIT_CREATOR, player);
    }

    public static boolean isInAnyNether(Player player) {
        return isIn(NETHER_SPAWN, player) || isIn(NETHER_PVP, player);
    }

    public static boolean isInAnyBuildable(Location location) {
        if (location.getWorld() == Utils.OVERWORLD) {
            return isIn(OW_PVP, location) || Duels.INSTANCE.overworldNormal.isIn(location) || Duels.INSTANCE.overworldFlat.isIn(location);
        }

        return isIn(NETHER_PVP, location) || Duels.INSTANCE.netherNormal.isIn(location) || Duels.INSTANCE.netherFlat.isIn(location);
    }

    public static Region toWERegion(ProtectedRegion region) {
        return new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());
    }
}
