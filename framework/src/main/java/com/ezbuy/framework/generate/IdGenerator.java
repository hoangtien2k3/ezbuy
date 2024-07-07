package com.ezbuy.framework.generate;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static final AtomicInteger idCounter = new AtomicInteger();

    public static int generateId() {
        return idCounter.incrementAndGet();
    }
}
