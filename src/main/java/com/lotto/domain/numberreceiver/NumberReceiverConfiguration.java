package com.lotto.domain.numberreceiver;


import com.lotto.domain.drowdate.DrawDateFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class NumberReceiverConfiguration {

    @Bean
    HashGenerable hashGenerable() {
        return new HashGenerator();
    }

    @Bean
    NumberReceiverFacade numberReceiverFacade(final TicketRepository repository,
                                              final DrawDateFacade drawDateFacade,
                                              final HashGenerable hashGenerator) {
        NumberValidator validator = new NumberValidator();
        return new NumberReceiverFacade(validator, hashGenerator, repository, drawDateFacade);
    }
}
