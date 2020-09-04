package minegame159.thebestplugin;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class Perms {
    private static final List<Permission> PERMS = new ArrayList<>();

    public static final Permission CHANGE_GAMEMODE = create("thebestplugin.change-gamemode");
    public static final Permission FEED = create("thebestplugin.feed");
    public static final Permission HEAL = create("thebestplugin.heal");
    public static final Permission TOGGLE_KIT_CREATOR = create("thebestplugin.toggle-kitcreator");
    public static final Permission CLEAR_ARENAS = create("thebestplugin.clear-arenas");

    public static final Permission UNLIMITED_KITS = create("thebestplugin.unlimited-kits");
    public static final Permission DELETE_KIT = create("thebestplugin.delete-kit");

    public static void register() {
        for (Permission perm : PERMS) {
            Bukkit.getPluginManager().addPermission(perm);
        }
    }

    private static Permission create(String name) {
        Permission perm = new Permission(name);
        PERMS.add(perm);
        return perm;
    }
}
