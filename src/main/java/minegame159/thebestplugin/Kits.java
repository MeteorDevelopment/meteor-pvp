package minegame159.thebestplugin;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class Kits {
    public static Kits INSTANCE;
    public static final String GUI_TITLE = "Kits";
    public static final String MSG_PREFIX = ChatColor.BLUE + "[Kits]" + ChatColor.GRAY + ": " + ChatColor.WHITE;

    private static File FILE;

    private static final List<String> LIST = new ArrayList<>();

    private final Map<String, Kit> kits = new HashMap<>();
    private final Map<Player, Boolean> usedKit = new HashMap<>();

    public Kits() {
        INSTANCE = this;
        FILE = new File(TheBestPlugin.CONFIG_FOLDER, "kits.json");

        if (FILE.exists()) {
            try {
                TheBestPlugin.GSON.fromJson(new FileReader(FILE), getClass());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void onEnable() {
        usedKit.clear();
    }

    private void save() {
        try {
            FileWriter writer = new FileWriter(FILE);
            TheBestPlugin.GSON.toJson(this, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasKitWithName(String name) {
        return kits.containsKey(name);
    }

    public void addKit(Kit kit) {
        kits.put(kit.name, kit);
        save();
    }

    public void deleteKit(String name) {
        if (kits.remove(name) != null) save();
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

    public Collection<Kit> getKits() {
        return kits.values();
    }

    public List<String> getNames() {
        return new ArrayList<>(kits.keySet());
    }

    public List<String> getNames(Player player) {
        LIST.clear();

        for (Kit kit : getKits()) {
            if (kit.author.equals(player.getUniqueId())) LIST.add(kit.name);
        }

        return LIST;
    }

    public int getCount(Player player) {
        UUID uuid = player.getUniqueId();
        int count = 0;

        for (Kit kit : getKits()) {
            if (kit.author.equals(uuid)) count++;
        }

        return count;
    }

    public boolean useKitCommand(Player player) {
        boolean usedKit = this.usedKit.getOrDefault(player, false);
        this.usedKit.put(player, true);
        return !usedKit;
    }

    public void clearUsedKit(Player player) {
        usedKit.remove(player);
    }
}
