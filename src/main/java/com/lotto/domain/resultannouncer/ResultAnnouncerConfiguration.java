package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultchecker.ResultCheckerFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
class ResultAnnouncerConfiguration {

    @Bean
    public static ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, AnnouncerRepository repository, Clock clock) {
        return new ResultAnnouncerFacade(resultCheckerFacade, repository, clock);
    }
}
