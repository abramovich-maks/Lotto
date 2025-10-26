package com.lotto.domain.drowdate;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class DrawDateFacade {

    private final DrawDateGenerator drawDateGenerator;

    public LocalDateTime getNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }
}