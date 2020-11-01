package minegame159.thebestplugin.kits;

import minegame159.thebestplugin.TheBestPlugin;
import minegame159.thebestplugin.utils.ISerializable;
import minegame159.thebestplugin.utils.Utils;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.*;

public enum Kits implements ISerializable<CompoundTag> {
    INSTANCE;

    public static final String GUI_TITLE = "Kits";
    public static final String GUI_PRIVATE_KITS_TITLE = "Private Kits";
    public static final String GUI_PUBLIC_KITS_TITLE = ChatColor.GOLD + "Public Kits";

    private static final List<Kit> EMPTY_LIST = new ArrayList<>(0);

    private static File FILE;

    private final Map<String, Kit> KITS = new HashMap<>();
    private final Map<UUID, List<Kit>> PLAYER_KITS = new HashMap<>();
    private final List<Kit> PUBLIC_KITS = new ArrayList<>();

    public void init() {
        FILE = new File(TheBestPlugin.CONFIG_FOLDER, "kits.nbt");

        if (FILE.exists()) {
            try {
                fromTag((CompoundTag) NBTUtil.read(FILE).getTag());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save() {
        try {
            NBTUtil.write(toTag(), FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void add(Kit kit, boolean save) {
        KITS.put(kit.name, kit);
        PLAYER_KITS.computeIfAbsent(kit.author, uuid -> new ArrayList<>(1)).add(kit);

        if (kit.isPublic) {
            PUBLIC_KITS.add(kit);
            PUBLIC_KITS.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
        }

        if (save) save();
    }

    public void add(Kit kit) {
        add(kit, true);
    }

    public Kit get(String name) {
        return KITS.get(name);
    }

    public List<Kit> getKits(UUID player) {
        List<Kit> kits = PLAYER_KITS.get(player);
        return kits != null ? kits : EMPTY_LIST;
    }

    public List<Kit> getKits(HumanEntity player) {
        return getKits(player.getUniqueId());
    }

    public boolean remove(String name) {
        Kit kit = KITS.remove(name);
        if (kit != null) {
            PLAYER_KITS.get(kit.author).remove(kit);
            PUBLIC_KITS.remove(kit);
            PUBLIC_KITS.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
            return true;
        }

        return false;
    }

    public boolean remove(Player player, String name) {
        List<Kit> kits = PLAYER_KITS.get(player.getUniqueId());
        if (kits != null) {
            for (Kit kit : kits) {
                if (kit.name.equals(name)) {
                    kits.remove(kit);
                    KITS.remove(name);
                    PUBLIC_KITS.remove(kit);
                    PUBLIC_KITS.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
                    return true;
                }
            }
        }

        return false;
    }

    public void addPublic(Kit kit) {
        PUBLIC_KITS.add(kit);
        PUBLIC_KITS.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
    }

    public void removePublic(Kit kit) {
        if (PUBLIC_KITS.remove(kit)) {
            PUBLIC_KITS.sort((o1, o2) -> Collator.getInstance().compare(o1.name, o2.name));
        }
    }

    // Gui

    public Inventory guiMain(HumanEntity player) {
        Inventory gui = Bukkit.createInventory(player, 9, GUI_TITLE);

        ItemStack privateKits = new ItemStack(Material.IRON_SWORD);
        Utils.setName(privateKits, GUI_PRIVATE_KITS_TITLE);
        gui.setItem(3, privateKits);

        ItemStack publicKits = new ItemStack(Material.GOLDEN_SWORD);
        Utils.setName(publicKits, GUI_PUBLIC_KITS_TITLE);
        gui.setItem(5, publicKits);

        Utils.fillPanes(gui);
        return gui;
    }

    public Inventory guiPrivateKits(UUID player, HumanEntity invHolder) {
        Inventory gui = Bukkit.createInventory(invHolder, 9, GUI_PRIVATE_KITS_TITLE);
        List<Kit> kits = getKits(player);

        for (int i = 0; i < 9; i++) {
            Kit kit = null;
            if (i < kits.size()) kit = kits.get(i);

            if (kit != null) {
                gui.setItem(i, newKitItemStack(player, kit));
            } else {
                ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                Utils.setName(itemStack, ChatColor.GRAY.toString() + kits.size() + " / " + MaxKits.get(player).count);
                gui.setItem(i, itemStack);
            }
        }

        return gui;
    }

    public Inventory guiPrivateKits(HumanEntity player) {
        return guiPrivateKits(player.getUniqueId(), player);
    }

    public Inventory guiPublicKits(HumanEntity player) {
        Inventory gui = Bukkit.createInventory(player, 9 * 6, GUI_PUBLIC_KITS_TITLE);

        for (int i = 0; i < Math.max(PUBLIC_KITS.size(), 9 * 6); i++) {
            if (i >= 9 * 6) break;

            if (i < PUBLIC_KITS.size()) {
                gui.setItem(i, newKitItemStack(player, PUBLIC_KITS.get(i)));
            } else {
                ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                Utils.setName(itemStack, ChatColor.GRAY + "No kit");
                gui.setItem(i, itemStack);
            }
        }

        return gui;
    }

    private ItemStack newKitItemStack(UUID player, Kit kit) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = itemStack.getItemMeta();

        KitStats.count(kit);

        meta.setDisplayName(ChatColor.AQUA + kit.name);
        List<String> lore = new ArrayList<>(11);
        lore.add("");
        lore.add(ChatColor.WHITE + "Totems: " + ChatColor.GRAY + KitStats.totems);
        lore.add(ChatColor.WHITE + "Egaps: " + ChatColor.GRAY + KitStats.egaps);
        lore.add(ChatColor.WHITE + "Xp Bottles: " + ChatColor.GRAY + KitStats.xpBottles);
        lore.add(ChatColor.WHITE + "Obsidian: " + ChatColor.GRAY + KitStats.obsidian);
        lore.add(ChatColor.WHITE + "Crystals: " + ChatColor.GRAY + KitStats.crystals);
        lore.add(ChatColor.WHITE + "Beds: " + ChatColor.GRAY + KitStats.beds);
        lore.add("");
        lore.add(ChatColor.GRAY + Bukkit.getOfflinePlayer(kit.author).getName());
        if (MaxKits.get(player).canHavePublic || kit.isPublic) {
            lore.add("");
            if (kit.isPublic) lore.add(ChatColor.GREEN + "Public");
            else if (MaxKits.get(player).canHavePublic && kit.author.equals(player)) lore.add(ChatColor.GRAY + "Right click to set as public.");
        }
        meta.setLore(lore);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private ItemStack newKitItemStack(HumanEntity player, Kit kit) {
        return newKitItemStack(player.getUniqueId(), kit);
    }

    // Serialization

    @Override
    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        ListTag<CompoundTag> kitsTag = new ListTag<>(CompoundTag.class);

        for (Kit kit : KITS.values()) {
            kitsTag.add(kit.toTag());
        }

        tag.put("kits", kitsTag);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        KITS.clear();
        PLAYER_KITS.clear();
        PUBLIC_KITS.clear();

        for (Tag<?> t : tag.getListTag("kits")) {
            add(new Kit((CompoundTag) t), false);
        }
    }
}
