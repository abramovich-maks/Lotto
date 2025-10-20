package com.lotto.domain.numberreceiver;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


class DrawDateGenerator {


    private static final LocalTime DRAW_TIME = LocalTime.of(12, 0, 0);
    private final Clock clock;

    public DrawDateGenerator(Clock clock) {
        this.clock = clock;
    }

    public LocalDateTime getNextDrawDate() {
        LocalDateTime now = LocalDateTime.now(clock);
        LocalDate today = now.toLocalDate();

        if (today.getDayOfWeek() == DayOfWeek.SATURDAY) {
            LocalDateTime drawDateTimeToday = LocalDateTime.of(today, DRAW_TIME);
            if (now.isBefore(drawDateTimeToday)) {
                return drawDateTimeToday;
            }
        }
        LocalDate nextSaturday = today.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.SATURDAY));
        return LocalDateTime.of(nextSaturday, DRAW_TIME);
    }
}