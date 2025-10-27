package com.lotto;

import com.lotto.domain.numbergenerator.WinningNumbersGeneratorFacadeConfigurationProperties;
import com.lotto.infrastructure.numbergenerator.http.RundomNumberGeneratorRestTemplateConfigurationsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WinningNumbersGeneratorFacadeConfigurationProperties.class, RundomNumberGeneratorRestTemplateConfigurationsProperties.class})
public class LottoSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoSpringBootApplication.class, args);
    }

}