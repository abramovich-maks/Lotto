package com.lotto.infrastructure.security.jwt.lotto;

import lombok.Builder;

@Builder
public record JwtResponseDto(String token) {
}