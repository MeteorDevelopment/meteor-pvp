package minegame159.meteorpvp.utils;

import net.querz.nbt.tag.Tag;

public interface ISerializable<T extends Tag<?>> {
    T toTag();

    void fromTag(T tag);
}
