package minegame159.thebestplugin.utils;

import net.querz.nbt.tag.Tag;

public interface ISerializable<T extends Tag<?>> {
    T toTag();

    void fromTag(T tag);
}
