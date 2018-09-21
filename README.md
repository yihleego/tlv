# Type-length-value

Within [data communication protocols](https://en.wikipedia.org/wiki/Data_communication_protocol), **TLV** (*type-length-value* or *tag-length-value*) is an encoding scheme used for optional information element in a certain protocol.
The type and length are fixed in size (typically 1-4 bytes), and the value field is of variable size. These fields are used as follows:

**Type**
A binary code, often simply alphanumeric, which indicates the kind of field that this part of the message represents;

**Length**
The size of the value field (typically in bytes);

**Value**
Variable-sized series of bytes which contains data for this part of the message.

Some advantages of using a TLV representation data system solution are:

> * TLV sequences are easily searched using generalized parsing functions;
> * New message elements which are received at an older node can be safely skipped and the rest of the message can be parsed. This is similar to the way that unknown XML tags can be safely skipped;
> * TLV elements can be placed in any order inside the message body;
> * TLV elements are typically used in a binary format which makes parsing faster and the data smaller;
> * It is easier to generate XML from TLV to make human inspection of the data possible.

## Brief

##### Sample TLV Object:

| 4 Bytes | 4 Bytes | Length Bytes  |
| :---:   | :---:   | :---:         |
| Type    | Length  | Value         |

##### Serial TLV Objects:

| 4 Bytes | 4 Bytes | Length Bytes | 4 Bytes | 4 Bytes | Length Bytes |
| :---:   | :---:   | :---:        | :---:   | :---:   | :---:        |
| Type    | Length  | Value        | Type    | Length  | Value        |

##### Nested TLV Objects:
| 4 Bytes | 4 Bytes | {           | Length Bytes | }                |
| :---:   | :---:   | :---:       | :---:        | :---:            |
|         |         | **4 Bytes** | **4 Bytes**  | **Length Bytes** |
| Type    | Length  | Type        | Length       | Value            |

## How to use

#### Serialize

```java
TlvBox inner = new TlvBox();
inner.put(0xFF, "Inner TlvBox");

TlvBox box = TlvBox.create()
        .put(0x00, true)
        .put(0x01, (byte) 1)
        .put(0x02, (short) 2)
        .put(0x03, 3)
        .put(0x04, (long) 4)
        .put(0x05, 5.5f)
        .put(0x06, 6.5)
        .put(0x07, 'A')
        .put(0x08, "Hello world!")
        .put(0x09, new byte[]{3, 4, 6, 3, 9})
        .put(0x10, inner);
byte[] bytes = box.serialize();
```

#### Parse

```java
TlvBox parsed = TlvBox.parse(bytes);

Boolean   v0 = parsed.getBoolean(0x00);
Byte      v1 = parsed.getByte(0x01);
Short     v2 = parsed.getShort(0x02);
Integer   v3 = parsed.getInteger(0x03);
Long      v4 = parsed.getLong(0x04);
Float     v5 = parsed.getFloat(0x05);
Double    v6 = parsed.getDouble(0x06);
Character v7 = parsed.getCharacter(0x07);
String    v8 = parsed.getString(0x08);
byte[]    v9 = parsed.getBytes(0x09);
TlvBox    v10 = parsed.getObject(0x10);
```

#### ConcurrentTlvBox

```java
AtomicInteger adder = new AtomicInteger();
ConcurrentTlvBox concurrentTlvBox = ConcurrentTlvBox.create();
List<Thread> list = new ArrayList<>();
for (int i = 0; i < 1000; i++) {
    list.add(new Thread(() -> {
        TimeUnit.MILLISECONDS.sleep(1);
        while (adder.incrementAndGet() < 100000000) {
            int random = (int) (Math.random() * 10000);
            concurrentTlvBox.put(random, 1);
        }
    }));
}
for (int i = 0; i < 1000; i++) {
    list.get(i).start();
}
while (adder.get() < 100000000) {
    TimeUnit.MILLISECONDS.sleep(1);
    concurrentTlvBox.serialize();
}
```
## Contact
> * Bugs: [Issues](https://github.com/yihleego/tlv/issues)

## Links
> * [Type-length-value Wiki](https://en.wikipedia.org/wiki/Type-length-value)
> * [Jhuster/TLV](https://github.com/Jhuster/TLV)
> * [Google/Protocol Buffers](https://github.com/protocolbuffers/protobuf)

## License
TLV framework is under the Do What The F*ck You Want To Public License. See the [LICENSE](https://github.com/yihleego/tlv/blob/master/LICENSE.txt) file for details.
