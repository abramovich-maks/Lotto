package com.lotto.infrastructure.security.jwt.lotto;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "auth.jwt")
public record JwtConfigurationProperties(
        String secret,
        long expirationMinutes,
        String issuer
) {
}