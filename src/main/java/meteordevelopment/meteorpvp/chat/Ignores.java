package meteordevelopment.meteorpvp.chat;

import meteordevelopment.meteorpvp.Config;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import net.querz.nbt.tag.Tag;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Ignores {
    private static final List<UUID> EMPTY = new ArrayList<>();
    private static final Map<UUID, List<UUID>> IGNORES = new HashMap<>();

    public static void load() {
        IGNORES.clear();

        File file = new File(Config.FOLDER, "ignores.nbt");

        if (file.exists()) {
            try {
                fromTag((CompoundTag) NBTUtil.read(file).getTag());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        File file = new File(Config.FOLDER, "ignores.nbt");
        file.getParentFile().mkdirs();

        try {
            NBTUtil.write(toTag(), file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private static CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();

        for (UUID player : IGNORES.keySet()) {
            ListTag<StringTag> list = new ListTag<>(StringTag.class);

            for (UUID ignored : IGNORES.get(player)) {
                list.add(new StringTag(ignored.toString()));
            }

            tag.put(player.toString(), list);
        }

        return tag;
    }

    private static void fromTag(CompoundTag tag) {
        for (String key : tag.keySet()) {
            UUID player = UUID.fromString(key);
            List<UUID> list = new ArrayList<>(tag.keySet().size());

            for (Tag<?> t : tag.getListTag(key)) {
                list.add(UUID.fromString(((StringTag) t).getValue()));
            }

            IGNORES.put(player, list);
        }
    }
}