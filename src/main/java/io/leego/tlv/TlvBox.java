package io.leego.tlv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yihleego
 */
public class TlvBox {
    protected static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;
    protected final Map<Integer, byte[]> valueMap;

    public TlvBox() {
        this.valueMap = new HashMap<>();
    }

    public TlvBox(TlvBox tlvBox) {
        this(tlvBox != null ? tlvBox.serialize() : null);
    }

    public TlvBox(byte[] buffer) {
        this(buffer, 0, buffer.length);
    }

    public TlvBox(byte[] buffer, int offset, int length) {
        this.valueMap = new HashMap<>();
        if (buffer == null) {
            return;
        }
        int parsed = 0;
        while (parsed < length) {
            int type = ByteBuffer.wrap(buffer, offset + parsed, 4).order(DEFAULT_BYTE_ORDER).getInt();
            parsed += 4;
            int size = ByteBuffer.wrap(buffer, offset + parsed, 4).order(DEFAULT_BYTE_ORDER).getInt();
            parsed += 4;
            byte[] value = new byte[size];
            System.arraycopy(buffer, offset + parsed, value, 0, size);
            put(type, value);
            parsed += size;
        }
    }

    public int length() {
        int length = 0;
        for (Map.Entry<Integer, byte[]> entry : valueMap.entrySet()) {
            length += entry.getValue().length + 8;
        }
        return length;
    }

    public static TlvBox create() {
        return new TlvBox();
    }

    public static TlvBox clone(TlvBox tlvBox) {
        if (tlvBox == null) {
            return null;
        }
        return new TlvBox(tlvBox.serialize());
    }

    public static TlvBox parse(byte[] buffer, int offset, int length) {
        if (buffer == null) {
            return null;
        }
        return new TlvBox(buffer, offset, length);
    }

    public static TlvBox parse(byte[] buffer) {
        if (buffer == null) {
            return null;
        }
        return new TlvBox(buffer);
    }

    public static byte[] serialize(TlvBox tlvBox) {
        if (tlvBox == null) {
            return null;
        }
        return tlvBox.serialize();
    }

    public byte[] serialize() {
        int offset = 0;
        int length = this.length();
        if (length == 0) {
            return new byte[0];
        }
        byte[] result = new byte[length];
        for (Map.Entry<Integer, byte[]> entry : valueMap.entrySet()) {
            Integer type = entry.getKey();
            byte[] value = entry.getValue();
            byte[] typeBytes = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(type).array();
            byte[] valueBytes = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(value.length).array();
            System.arraycopy(typeBytes, 0, result, offset, typeBytes.length);
            offset += 4;
            System.arraycopy(valueBytes, 0, result, offset, valueBytes.length);
            offset += 4;
            System.arraycopy(value, 0, result, offset, value.length);
            offset += value.length;
        }
        return result;
    }

    public TlvBox put(int type, byte[] value) {
        if (value == null) {
            return this;
        }
        valueMap.put(type, value);
        return this;
    }

    public TlvBox put(int type, byte value) {
        return put(type, new byte[]{value});
    }

    public TlvBox put(int type, boolean value) {
        return put(type, new byte[]{value ? (byte) 1 : (byte) 0});
    }

    public TlvBox put(int type, char value) {
        return put(type, ByteBuffer.allocate(2).order(DEFAULT_BYTE_ORDER).putChar(value).array());
    }

    public TlvBox put(int type, short value) {
        return put(type, ByteBuffer.allocate(2).order(DEFAULT_BYTE_ORDER).putShort(value).array());
    }

    public TlvBox put(int type, int value) {
        return put(type, ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(value).array());
    }

    public TlvBox put(int type, long value) {
        return put(type, ByteBuffer.allocate(8).order(DEFAULT_BYTE_ORDER).putLong(value).array());
    }

    public TlvBox put(int type, float value) {
        return put(type, ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putFloat(value).array());
    }

    public TlvBox put(int type, double value) {
        return put(type, ByteBuffer.allocate(8).order(DEFAULT_BYTE_ORDER).putDouble(value).array());
    }

    public TlvBox put(int type, String value) {
        if (value == null) {
            return this;
        }
        return put(type, value.getBytes(StandardCharsets.UTF_8));
    }

    public TlvBox put(int type, TlvBox value) {
        if (value == null) {
            return this;
        }
        return put(type, value.serialize());
    }

    public byte[] getBytes(int type) {
        return valueMap.get(type);
    }

    public Byte getByte(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return bytes[0];
    }

    public Boolean getBoolean(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return bytes[0] == 1;
    }

    public Character getCharacter(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getChar();
    }

    public Short getShort(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getShort();
    }

    public Integer getInteger(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getInt();
    }

    public Long getLong(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getLong();
    }

    public Float getFloat(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getFloat();
    }

    public Double getDouble(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getDouble();
    }

    public String getString(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public TlvBox getObject(int type) {
        byte[] bytes = getBytes(type);
        if (bytes == null) {
            return null;
        }
        return new TlvBox(bytes, 0, bytes.length);
    }

    public boolean contains(int type) {
        return valueMap.containsKey(type);
    }

    public byte[] remove(int type) {
        return valueMap.remove(type);
    }

}