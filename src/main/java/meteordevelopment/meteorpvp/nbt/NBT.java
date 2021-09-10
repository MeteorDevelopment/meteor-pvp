package meteordevelopment.meteorpvp.nbt;

import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import org.bukkit.NamespacedKey;

public class NBT {
    public static void toTag(NbtWriter nbt, String name, NamespacedKey key) {
        nbt.writeList(name, NbtTag.String, 2);
            nbt.writeString(key.getNamespace());
            nbt.writeString(key.getKey());
    }

    public static NamespacedKey toKey(ListTag<?> tag) {
        return new NamespacedKey(((StringTag) tag.get(0)).getValue(), ((StringTag) tag.get(1)).getValue());
    }
}
