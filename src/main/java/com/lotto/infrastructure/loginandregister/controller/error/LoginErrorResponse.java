package com.lotto.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;

public record LoginErrorResponse(
        String message,
        HttpStatus status
) {
}
