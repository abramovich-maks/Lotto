package com.lotto.domain.drowdate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class DrawDateConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
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
