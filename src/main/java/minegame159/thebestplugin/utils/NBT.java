package minegame159.thebestplugin.utils;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;

import java.io.IOException;

public class NBT {
    private static final NBTSerializer SERIALIZER = new NBTSerializer();
    private static final NBTDeserializer DESERIALIZER = new NBTDeserializer();

    public static byte[] toBytes(CompoundTag tag) {
        try {
            return SERIALIZER.toBytes(new NamedTag(null, tag));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    public static CompoundTag fromBytes(byte[] bytes) {
        try {
            return (CompoundTag) DESERIALIZER.fromBytes(bytes).getTag();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new CompoundTag();
    }
}
