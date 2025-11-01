package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultchecker.ResultCheckerFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ResultAnnouncerConfiguration {

    @Bean
    public static ResultAnnouncerFacade resultAnnouncerFacade(ResultCheckerFacade resultCheckerFacade, AnnouncerRepository repository) {
        return new ResultAnnouncerFacade(resultCheckerFacade, repository);
    }
}
