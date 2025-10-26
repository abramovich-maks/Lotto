package com.lotto.domain.drowdate;

import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

@AllArgsConstructor
class DrawDateGeneratorTestImpl implements DrawDateGenerable {

    private final Clock clock;

    @Override
    public LocalDateTime getNextDrawDate() {
        return new DrawDateGenerator(clock).getNextDrawDate();
    }
}
