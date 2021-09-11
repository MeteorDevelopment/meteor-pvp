package meteordevelopment.meteorpvp.chat;

import meteordevelopment.meteorpvp.Config;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mutes {
    private static final List<UUID> MUTES = new ArrayList<>();

    public static void load() {
        MUTES.clear();

        File file = new File(Config.FOLDER, "mutes.nbt");

        if (file.exists()) {
            try {
                fromTag((CompoundTag) NBTUtil.read(file).getTag());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        File file = new File(Config.FOLDER, "mutes.nbt");
        file.getParentFile().mkdirs();

        try {
            NBTUtil.write(toTag(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        ListTag<StringTag> list = new ListTag<>(StringTag.class);

        for (UUID player : MUTES) list.add(new StringTag(player.toString()));

        tag.put("mutes", list);

        return tag;
    }

    private static void fromTag(CompoundTag tag) {
        for (Tag<?> t : tag.getListTag("mutes")) {
            MUTES.add(UUID.fromString(((StringTag) t).getValue()));
        }
    }
}
