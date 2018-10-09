package com.leego.standard.tlv;

/**
 * Created by YihLeego on 2018.09.07 19:30
 *
 * @author YihLeego
 * @version 1.0.0
 */
public class ConcurrentTlvBox extends TlvBox {

    public ConcurrentTlvBox() {
        super();
    }

    public ConcurrentTlvBox(int initialCapacity) {
        super(initialCapacity);
    }

    public ConcurrentTlvBox(TlvBox tlvBox) {
        super(tlvBox);
    }

    public ConcurrentTlvBox(byte[] buffer) {
        super(buffer);
    }

    public ConcurrentTlvBox(byte[] buffer, int offset, int length) {
        super(buffer, offset, length);
    }

    public static ConcurrentTlvBox create() {
        return new ConcurrentTlvBox();
    }

    public static ConcurrentTlvBox create(int initialCapacity) {
        return new ConcurrentTlvBox(initialCapacity);
    }

    public static ConcurrentTlvBox clone(TlvBox tlvBox) {
        if (tlvBox == null) {
            return null;
        }
        return new ConcurrentTlvBox(tlvBox.serialize());
    }

    public static ConcurrentTlvBox parse(byte[] buffer, int offset, int length) {
        if (buffer == null) {
            return null;
        }
        return new ConcurrentTlvBox(buffer, offset, length);
    }

    public static ConcurrentTlvBox parse(byte[] buffer) {
        if (buffer == null) {
            return null;
        }
        return new ConcurrentTlvBox(buffer);
    }

    public static byte[] serialize(TlvBox tlvBox) {
        if (tlvBox == null) {
            return null;
        }
        return tlvBox.serialize();
    }

    @Override
    public synchronized byte[] serialize() {
        return super.serialize();
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, byte[] value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, byte value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, boolean value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, char value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, short value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, int value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, long value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, float value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, double value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, String value) {
        super.put(type, value);
        return this;
    }

    @Override
    public synchronized ConcurrentTlvBox put(int type, TlvBox value) {
        super.put(type, value);
        return this;
    }

    @Override
    public ConcurrentTlvBox getObject(int type) {
        byte[] bytes = this.getBytes(type);
        if (bytes == null) {
            return null;
        }
        return ConcurrentTlvBox.parse(bytes, 0, bytes.length);
    }

}