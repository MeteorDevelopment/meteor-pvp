package minegame159.meteorpvp.nbt;

import net.querz.nbt.tag.Tag;

public interface ISerializable<T extends Tag<?>> {
    void toTag(NbtWriter nbt);

    void fromTag(T tag);
}
