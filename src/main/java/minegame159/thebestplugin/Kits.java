package minegame159.thebestplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.*;
import java.text.Collator;
import java.util.*;

public class Kits {
    public static final Kits INSTANCE = new Kits();
    public static final String GUI_TITLE = "Kits";

    private static File FILE;

    private static final List<String> LIST = new ArrayList<>();

    private final Map<String, Kit> kits = new HashMap<>();
    private final List<Kit> kitsSorted = new ArrayList<>();

    private Kits() {}

    public void init() {
        FILE = new File(TheBestPlugin.CONFIG_FOLDER, "kits.json");

        kits.clear();
        kitsSorted.clear();

        if (FILE.exists()) {
            try {
                TheBestPlugin.GSON.fromJson(new FileReader(FILE), getClass());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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

    public void addKit(Kit kit, boolean save) {
        kits.put(kit.name, kit);

        kitsSorted.add(kit);
        kitsSorted.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));

        if (save) save();
    }
    public void addKit(Kit kit) { addKit(kit, true); }

    public void deleteKit(String name) {
        Kit kit = kits.remove(name);
        if (kit != null) {
            kitsSorted.remove(kit);
            kitsSorted.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));

            save();
        }
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

    public List<Kit> getKits() {
        return kitsSorted;
    }

    public int getMaxPage() {
        return (int) Math.ceil(kits.size() / (9.0 * 5));
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

    // GUI

    public void fillGui(Inventory gui, int page) {
        gui.clear();

        int pageSize = 9 * 5;
        int pageStart = (page - 1) * pageSize;

        int slotI = 0;
        for (int i = pageStart; i < pageStart + pageSize; i++) {
            if (i >= kitsSorted.size()) break;
            Kit kit = kitsSorted.get(i);

            ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = itemStack.getItemMeta();

            meta.setDisplayName(ChatColor.AQUA + kit.name);
            List<String> lore = new ArrayList<>(1);
            lore.add("");
            lore.add(ChatColor.WHITE + "Totems: " + ChatColor.GRAY + kit.count(Material.TOTEM_OF_UNDYING));
            lore.add(ChatColor.WHITE + "Egaps: " + ChatColor.GRAY + kit.count(Material.ENCHANTED_GOLDEN_APPLE));
            lore.add(ChatColor.WHITE + "Xp Bottles: " + ChatColor.GRAY + kit.count(Material.EXPERIENCE_BOTTLE));
            lore.add(ChatColor.WHITE + "Obsidian: " + ChatColor.GRAY + kit.count(Material.OBSIDIAN));
            lore.add(ChatColor.WHITE + "Crystals: " + ChatColor.GRAY + kit.count(Material.END_CRYSTAL));
            lore.add(ChatColor.WHITE + "Beds: " + ChatColor.GRAY + kit.countBeds());
            lore.add("");
            lore.add(ChatColor.GRAY + Bukkit.getOfflinePlayer(kit.author).getName());
            meta.setLore(lore);

            itemStack.setItemMeta(meta);
            gui.setItem(slotI, itemStack);

            if (page > 1) {
                ItemStack left = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta leftMeta = (SkullMeta) left.getItemMeta();
                leftMeta.setDisplayName("Previous");
                leftMeta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowLeft"));
                left.setItemMeta(leftMeta);

                gui.setItem(9 * 5 + 2, left);
            }

            ItemStack pageItem = new ItemStack(Material.PAPER);
            ItemMeta pageItemMeta = pageItem.getItemMeta();
            pageItemMeta.setDisplayName("Page " + page);
            pageItem.setItemMeta(pageItemMeta);
            gui.setItem(9 * 5 + 4, pageItem);

            if (page < Kits.INSTANCE.getMaxPage()) {
                ItemStack right = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta rightMeta = (SkullMeta) right.getItemMeta();
                rightMeta.setDisplayName("Next");
                rightMeta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_ArrowRight"));
                right.setItemMeta(rightMeta);

                gui.setItem(9 * 5 + 6, right);
            }

            slotI++;
        }
    }
}
