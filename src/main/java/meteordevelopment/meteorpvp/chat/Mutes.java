package meteordevelopment.meteorpvp.chat;

import meteordevelopment.meteorpvp.Config;
import meteordevelopment.nbt.NBT;
import meteordevelopment.nbt.NamedTag;
import meteordevelopment.nbt.NbtFormatException;
import meteordevelopment.nbt.tags.CompoundTag;
import meteordevelopment.nbt.tags.ListTag;
import meteordevelopment.nbt.tags.StringTag;
import meteordevelopment.nbt.tags.TagId;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mutes {
    private static final List<UUID> MUTES = new ArrayList<>();
    private static File FILE;

    public static boolean addMute(OfflinePlayer player) {
        if (MUTES.contains(player.getUniqueId())) return false;

        MUTES.add(player.getUniqueId());
        save();
        return true;
    }

    public static boolean removeMute(Player player) {
        if (!MUTES.contains(player.getUniqueId())) return false;

        MUTES.remove(player.getUniqueId());
        save();
        return true;
    }

    public static boolean isMuted(Player player) {
        return MUTES.contains(player.getUniqueId());
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
        MUTES.clear();

        for (StringTag tag : namedTag.tag.getList("mutes", StringTag.class)) {
            MUTES.add(UUID.fromString(tag.toString()));
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

        ListTag<StringTag> list = new ListTag<>(TagId.String);
        for (UUID uuid : MUTES) list.add(new StringTag(uuid.toString()));
        tag.put("mutes", list);

        return tag;
    }
}
