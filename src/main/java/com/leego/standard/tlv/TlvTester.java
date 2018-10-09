package com.leego.standard.tlv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by YihLeego on 2018.09.07 21:14
 *
 * @author YihLeego
 * @version 1.0.0
 */
public class TlvTester {
    private static final Logger logger = Logger.getLogger("TLV");
    private static final int TEST_TYPE_0 = 0x00;
    private static final int TEST_TYPE_1 = 0x01;
    private static final int TEST_TYPE_2 = 0x02;
    private static final int TEST_TYPE_3 = 0x03;
    private static final int TEST_TYPE_4 = 0x04;
    private static final int TEST_TYPE_5 = 0x05;
    private static final int TEST_TYPE_6 = 0x06;
    private static final int TEST_TYPE_7 = 0x07;
    private static final int TEST_TYPE_8 = 0x08;
    private static final int TEST_TYPE_9 = 0x09;
    private static final int TEST_TYPE_10 = 0x10;

    public static void main(String[] args) {
        TlvBox inner = TlvBox.create()
                .put(TEST_TYPE_0, true)
                .put(TEST_TYPE_1, (byte) 1)
                .put(TEST_TYPE_2, (short) 2)
                .put(TEST_TYPE_3, 3)
                .put(TEST_TYPE_4, (long) 4)
                .put(TEST_TYPE_5, 5.67f)
                .put(TEST_TYPE_6, 8.91)
                .put(TEST_TYPE_7, 'A')
                .put(TEST_TYPE_8, "hello world!")
                .put(TEST_TYPE_9, new byte[]{3, 4, 6, 3, 9});

        TlvBox outer = TlvBox.create().put(TEST_TYPE_10, inner);

        byte[] bytes = outer.serialize();

        TlvBox parsedOuter = TlvBox.parse(bytes);
        TlvBox parsedInner = parsedOuter.getObject(TEST_TYPE_10);

        logger.info("TEST_TYPE_0: " + parsedInner.getBoolean(TEST_TYPE_0));
        logger.info("TEST_TYPE_1: " + parsedInner.getByte(TEST_TYPE_1));
        logger.info("TEST_TYPE_2: " + parsedInner.getShort(TEST_TYPE_2));
        logger.info("TEST_TYPE_3: " + parsedInner.getInteger(TEST_TYPE_3));
        logger.info("TEST_TYPE_4: " + parsedInner.getLong(TEST_TYPE_4));
        logger.info("TEST_TYPE_5: " + parsedInner.getFloat(TEST_TYPE_5));
        logger.info("TEST_TYPE_6: " + parsedInner.getDouble(TEST_TYPE_6));
        logger.info("TEST_TYPE_7: " + parsedInner.getCharacter(TEST_TYPE_7));
        logger.info("TEST_TYPE_8: " + parsedInner.getString(TEST_TYPE_8));
        logger.info("TEST_TYPE_9: " + Arrays.toString(parsedInner.getBytes(TEST_TYPE_9)));

        AtomicInteger adder = new AtomicInteger();
        TlvBox tlvBox = TlvBox.create();
        ConcurrentTlvBox concurrentTlvBox = ConcurrentTlvBox.create();
        List<Thread> tlvBoxThreadList = new ArrayList<>();
        List<Thread> concurrentTlvBoxThreadList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tlvBoxThreadList.add(new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (adder.incrementAndGet() < 100000000) {
                    int random = (int) (Math.random() * 10000);
                    tlvBox.put(random, 1);
                }
            }));
            concurrentTlvBoxThreadList.add(new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (adder.incrementAndGet() < 100000000) {
                    int random = (int) (Math.random() * 10000);
                    concurrentTlvBox.put(random, 1);
                }
            }));
        }
        for (int i = 0; i < 1000; i++) {
            tlvBoxThreadList.get(i).start();
            concurrentTlvBoxThreadList.get(i).start();
        }
        while (adder.get() < 100000000) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
