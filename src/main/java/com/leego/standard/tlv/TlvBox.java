package com.leego.standard.tlv;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YihLeego on 2018.09.07 19:30
 *
 * @author YihLeego
 * @version 1.0.0
 */
public class TlvBox {
    protected static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    private Map<Integer, byte[]> valueMap;
    private int length;

    public TlvBox() {
        valueMap = new HashMap<>();
    }

    public TlvBox(int initialCapacity) {
        valueMap = new HashMap<>(initialCapacity);
    }

    public TlvBox(TlvBox tlvBox) {
        this(tlvBox == null ? null : tlvBox.serialize());
    }

    public TlvBox(byte[] buffer) {
        this(buffer, 0, buffer.length);
    }

    public TlvBox(byte[] buffer, int offset, int length) {
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

    public static TlvBox create() {
        return new TlvBox();
    }

    public static TlvBox create(int initialCapacity) {
        return new TlvBox(initialCapacity);
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

    /**
     * This method is not synchronized,
     * when multiple threads put values concurrently.
     * <p>It <i>must</i> be synchronized externally.
     *
     * @return {@code byte[]} bytes
     */
    public byte[] serialize() {
        int offset = 0;
        byte[] result = new byte[length];
        for (Map.Entry<Integer, byte[]> entry : valueMap.entrySet()) {
            Integer type = entry.getKey();
            byte[] valueBytes = entry.getValue();
            byte[] typeBytes = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(type).array();
            byte[] lengthBytes = ByteBuffer.allocate(4).order(DEFAULT_BYTE_ORDER).putInt(valueBytes.length).array();
            System.arraycopy(typeBytes, 0, result, offset, typeBytes.length);
            offset += 4;
            System.arraycopy(lengthBytes, 0, result, offset, lengthBytes.length);
            offset += 4;
            System.arraycopy(valueBytes, 0, result, offset, valueBytes.length);
            offset += valueBytes.length;
        }
        return result;
    }

    /**
     * This method is not synchronized,
     * when multiple threads put a same-type and nonexistent value concurrently,
     * the length may be error.
     * <p>It <i>must</i> be synchronized externally.
     *
     * @param type  type
     * @param value value
     * @return a reference to this object {@link TlvBox}
     */
    public TlvBox put(int type, byte[] value) {
        if (value == null) {
            return this;
        }
        if (valueMap.containsKey(type)) {
            length += value.length - valueMap.get(type).length;
        } else {
            length += value.length + 8;
        }
        valueMap.put(type, value);
        return this;
    }

    public TlvBox put(int type, byte value) {
        return put(type, new byte[]{value});
    }

    public TlvBox put(int type, boolean value) {
        byte b;
        if (value) {
            b = 1;
        } else {
            b = 0;
        }
        return put(type, new byte[]{b});
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
        return put(type, value.getBytes());
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
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return bytes[0];
    }

    public Boolean getBoolean(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return bytes[0] == 1;
    }

    public Character getCharacter(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getChar();
    }

    public Short getShort(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getShort();
    }

    public Integer getInteger(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getInt();
    }

    public Long getLong(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getLong();
    }

    public Float getFloat(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getFloat();
    }

    public Double getDouble(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return ByteBuffer.wrap(bytes).order(DEFAULT_BYTE_ORDER).getDouble();
    }

    public String getString(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return new String(bytes);
    }

    public TlvBox getObject(int type) {
        byte[] bytes = valueMap.get(type);
        if (bytes == null) {
            return null;
        }
        return TlvBox.parse(bytes, 0, bytes.length);
    }

    public int size() {
        return length;
    }

}