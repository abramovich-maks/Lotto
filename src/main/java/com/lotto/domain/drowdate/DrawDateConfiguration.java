package com.lotto.domain.drowdate;

import com.lotto.domain.AdjustableClock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Configuration
class DrawDateConfiguration {

    @Bean
    AdjustableClock clock() {
        ZoneId POLAND = ZoneId.of("Europe/Warsaw");
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 10, 23, 12, 0, 0, 0, POLAND);
        return new AdjustableClock(zonedDateTime.toInstant(), POLAND);
    }

    @Bean
    DrawDateFacade drawDateFacade(Clock clock) {
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new DrawDateFacade(drawDateGenerator);
    }

    public DrawDateFacade drawDateFacadeForTest(Clock clock) {
        return drawDateFacade(clock);
    }
}
