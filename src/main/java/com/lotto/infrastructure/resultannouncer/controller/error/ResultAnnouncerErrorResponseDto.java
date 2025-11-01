package com.lotto.infrastructure.resultannouncer.controller.error;

import org.springframework.http.HttpStatus;

public record ResultAnnouncerErrorResponseDto(
        String message,
        HttpStatus status) {
}
