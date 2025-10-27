package com.lotto.domain.numbergenerator;

import com.lotto.domain.drowdate.DrawDateFacade;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class NumberGeneratorConfiguration {

    @Bean
    NumberGeneratorFacade numberGeneratorFacade(DrawDateFacade drawDateFacade, RandomNumberGenerable generator, WinningNumbersRepository winningNumbersRepository, WinningNumbersGeneratorFacadeConfigurationProperties properties) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new NumberGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, winningNumberValidator, properties);
    }

    public NumberGeneratorFacade numberGeneratorFacadeForTest(RandomNumberGenerable generator, DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository) {
        WinningNumbersGeneratorFacadeConfigurationProperties properties = WinningNumbersGeneratorFacadeConfigurationProperties.builder()
                .upperBand(99)
                .lowerBand(1)
                .count(6)
                .build();
        return numberGeneratorFacade(drawDateFacade, generator, winningNumbersRepository, properties);
    }
}
