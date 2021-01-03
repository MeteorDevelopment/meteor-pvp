package minegame159.meteorpvp.utils;

import net.querz.nbt.tag.ListTag;
import net.querz.nbt.tag.StringTag;
import org.bukkit.NamespacedKey;

public class NBT {
    public static ListTag<StringTag> toTag(NamespacedKey key) {
        ListTag<StringTag> tag = new ListTag<>(StringTag.class);

        tag.add(new StringTag(key.getNamespace()));
        tag.add(new StringTag(key.getKey()));

        return tag;
    }

    public static NamespacedKey toKey(ListTag<?> tag) {
        return new NamespacedKey(((StringTag) tag.get(0)).getValue(), ((StringTag) tag.get(1)).getValue());
    }
}
