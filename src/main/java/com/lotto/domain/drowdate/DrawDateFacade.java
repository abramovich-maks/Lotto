package com.lotto.domain.drowdate;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class DrawDateFacade {

    private final DrawDateGenerable drawDateGenerator;

    public LocalDateTime getNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }
}