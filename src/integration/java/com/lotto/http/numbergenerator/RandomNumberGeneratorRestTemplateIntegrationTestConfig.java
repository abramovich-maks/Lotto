package com.lotto.http.numbergenerator;

import com.lotto.domain.numbergenerator.RandomNumberGenerable;
import com.lotto.infrastructure.numbergenerator.http.RandomGeneratorClientConfig;
import com.lotto.infrastructure.numbergenerator.http.RandomNumberGeneratorRestTemplateConfigurationProperties;
import com.lotto.infrastructure.numbergenerator.http.RestTemplateResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class RandomNumberGeneratorRestTemplateIntegrationTestConfig extends RandomGeneratorClientConfig {

    public RandomNumberGeneratorRestTemplateIntegrationTestConfig(RandomNumberGeneratorRestTemplateConfigurationProperties properties) {
        super(properties);
    }

    RandomNumberGenerable remoteNumberGeneratorClient() {
        RestTemplate restTemplate = restTemplate(new RestTemplateResponseErrorHandler());
        return remoteNumberGeneratorClient(restTemplate);
    }
}
