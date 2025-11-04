package com.lotto.domain.numbergenerator;

import com.lotto.domain.drowdate.DrawDateFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class NumberGeneratorConfiguration {

    @Bean
    NumberGeneratorFacade numberGeneratorFacade(DrawDateFacade drawDateFacade, RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, WinningNumbersGeneratorFacadeConfigurationProperties properties, NumberReceiverFacade numberReceiverFacade) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new NumberGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, winningNumberValidator, properties, numberReceiverFacade);
    }

    public NumberGeneratorFacade numberGeneratorFacadeForTest(RandomNumberGenerable generator, DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository, NumberReceiverFacade numberReceiverFacade) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .upperBand(99)
                .lowerBand(1)
                .count(6)
                .build();
        return numberGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, properties, numberReceiverFacade);
    }
}
