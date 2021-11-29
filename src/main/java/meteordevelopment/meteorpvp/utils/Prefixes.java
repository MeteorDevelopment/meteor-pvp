package meteordevelopment.meteorpvp.utils;

import org.bukkit.ChatColor;

public class Prefixes {
    public static final String MPVP = get("Meteor PvP", ChatColor.RED);
    public static final String ARENA = get("Arena", ChatColor.GREEN);
    public static final String KITS = get("Kits", ChatColor.BLUE);
    public static final String DUELS = get("Duels", ChatColor.LIGHT_PURPLE);
    public static final String MUTES = get("Mutes", ChatColor.DARK_RED);
    public static final String IGNORES = get("Ignores", ChatColor.AQUA);

    private static String get(String name, ChatColor color) {
        return ChatColor.GRAY + "[" + color + name + ChatColor.GRAY + "] " + ChatColor.WHITE;
    }
}
