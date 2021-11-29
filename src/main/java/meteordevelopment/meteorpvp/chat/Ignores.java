package meteordevelopment.meteorpvp.chat;

import meteordevelopment.meteorpvp.Config;
import meteordevelopment.nbt.NBT;
import meteordevelopment.nbt.NamedTag;
import meteordevelopment.nbt.NbtFormatException;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.ListTag;
import meteordevelopment.nbt.tags.StringTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Ignores {
    private static final List<UUID> EMPTY = new ArrayList<>();
    private static final Map<UUID, List<UUID>> IGNORES = new HashMap<>();
    private static File FILE;

    public static boolean hasReceiverIgnored(Player sender, Player receiver) {
        List<UUID> list = IGNORES.get(receiver.getUniqueId());
        if (list == null) return false;
        return list.contains(sender.getUniqueId());
    }

    // Returns true if added to ignore list
    public static boolean toggleIgnore(Player player, UUID toIgnore) {
        List<UUID> list = IGNORES.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        if (!list.remove(toIgnore)) {
            list.add(toIgnore);
            return true;
        }
        return false;
    }

    public static List<UUID> getIgnores(Player player) {
        return IGNORES.getOrDefault(player.getUniqueId(), EMPTY);
    }

    // Files

    public static void load() {
        FILE = new File(Config.FOLDER, "ignores.nbt");

        if (FILE.exists()) {
            try {
                fromTag(NBT.read(FILE));
            } catch (NbtFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private static void fromTag(NamedTag namedTag) {
        IGNORES.clear();

        for (String key : namedTag.tag.keySet()) {
            UUID player = UUID.fromString(key);
            List<UUID> list = new ArrayList<>(namedTag.tag.keySet().size());

            for (StringTag ignored : namedTag.tag.getList(key, StringTag.class)) {
                list.add(UUID.fromString(ignored.toString()));
            }

            IGNORES.put(player, list);
        }
    }

    public static void save() {
        try {
            if (!FILE.exists()) {
                FILE.getParentFile().mkdirs();
                FILE.createNewFile();
            }
            NBT.write(toTag(), FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        for (UUID player : IGNORES.keySet()) {
            ListTag<StringTag> list = new ListTag<>(TagId.String);

            for (UUID ignored : IGNORES.get(player)) {
                list.add(new StringTag(ignored.toString()));
            }

            tag.put(player.toString(), list);
        }

        return tag;
    }
}