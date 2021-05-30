package minegame159.meteorpvp;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

public class Perms {
    private static final List<Permission> PERMS = new ArrayList<>();

    public static final Permission ADMIN = create("thebestplugin.admin");
    public static final Permission MODERATOR = create("thebestplugin.moderator");
    public static final Permission DONATOR = create("thebestplugin.donator");

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
