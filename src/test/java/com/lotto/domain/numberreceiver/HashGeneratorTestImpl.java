package com.lotto.domain.numberreceiver;

import java.util.concurrent.atomic.AtomicInteger;

public class HashGeneratorTestImpl implements HashGenerable {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final String hash;

    public HashGeneratorTestImpl(String hash) {
        this.hash = hash;
    }

    public HashGeneratorTestImpl() {
        this("hash");
    }

    @Override
    public String getHash() {
        return hash + "-" + counter.incrementAndGet();
    }
}
