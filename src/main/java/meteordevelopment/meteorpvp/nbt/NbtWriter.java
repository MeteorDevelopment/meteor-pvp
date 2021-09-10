package meteordevelopment.meteorpvp.nbt;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NbtWriter {
    private final OutputStream stream;
    private final DataOutput out;

    public NbtWriter(OutputStream stream) {
        this.stream = stream;
        this.out = new DataOutputStream(stream);

        writeCompoundStart("");
    }

    public void writeCompoundStart(String name) {
        try {
            writeHeader(NbtTag.Compound, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeCompoundStart() { writeCompoundStart(null); }

    public void writeList(String name, NbtTag type, int size) {
        try {
            writeHeader(NbtTag.List, name);
            out.writeByte(type.ordinal());
            out.writeInt(size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeList(NbtTag type, int size) { writeList(null, type, size); }

    public void writeByte(String name, byte v) {
        try {
            writeHeader(NbtTag.Byte, name);
            out.writeByte(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeByte(byte v) { writeByte(null, v); }

    public void writeBool(String name, boolean v) {
        writeByte(name, (byte) (v ? 1 : 0));
    }
    public void writeBool(boolean v) { writeBool(null, v); }

    public void writeShort(String name, short v) {
        try {
            writeHeader(NbtTag.Short, name);
            out.writeShort(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeShort(short v) { writeShort(null, v); }

    public void writeInt(String name, int v) {
        try {
            writeHeader(NbtTag.Int, name);
            out.writeInt(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeInt(int v) { writeInt(null, v); }

    public void writeLong(String name, long v) {
        try {
            writeHeader(NbtTag.Long, name);
            out.writeLong(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeLong(long v) { writeLong(null, v); }

    public void writeFloat(String name, float v) {
        try {
            writeHeader(NbtTag.Float, name);
            out.writeFloat(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeFloat(float v) { writeFloat(null, v); }

    public void writeDouble(String name, double v) {
        try {
            writeHeader(NbtTag.Double, name);
            out.writeDouble(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeDouble(double v) { writeDouble(null, v); }

    public void writeByteArray(String name, byte[] v) {
        try {
            writeHeader(NbtTag.ByteArray, name);
            out.writeInt(v.length);
            out.write(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeByteArray(byte[] v) { writeByteArray(null, v); }

    public void writeString(String name, String v) {
        try {
            writeHeader(NbtTag.String, name);
            out.writeUTF(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeString(String v) { writeString(null, v); }

    public void writeIntArray(String name, int[] v) {
        try {
            writeHeader(NbtTag.IntArray, name);
            out.writeInt(v.length);
            for (int j : v) out.writeInt(j);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeIntArray(int[] v) { writeIntArray(null, v); }

    public void writeLongArray(String name, long[] v) {
        try {
            writeHeader(NbtTag.LongArray, name);
            out.writeInt(v.length);
            for (long l : v) out.writeLong(l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeLongArray(long[] v) { writeLongArray(null, v); }

    public void writeCompoundEnd() {
        try {
            out.writeByte(NbtTag.End.ordinal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        writeCompoundEnd();

        try {
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeader(NbtTag type, String name) throws IOException {
        if (name != null) {
            out.writeByte(type.ordinal());
            out.writeUTF(name);
        }
    }
}
