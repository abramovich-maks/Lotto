package com.lotto.infrastructure.security.jwt.lotto.error;

import org.springframework.http.HttpStatus;

public record LoginErrorResponse(
        String message,
        HttpStatus status
) {
}
