package com.lotto;

import com.lotto.domain.numbergenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import com.lotto.infrastructure.numbergenerator.http.RundomNumberGeneratorRestTemplateConfigurationsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersGeneratorFacadeConfigurationProperties.class, RundomNumberGeneratorRestTemplateConfigurationsProperties.class})
@EnableScheduling
@
public class LottoSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }

}