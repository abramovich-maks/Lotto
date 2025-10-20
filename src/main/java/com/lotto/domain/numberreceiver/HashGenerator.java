package com.lotto.domain.numberreceiver;

import java.util.UUID;

class HashGenerator implements HashGenerable{

    public String getHash() {
        return UUID.randomUUID().toString();
    }
}
