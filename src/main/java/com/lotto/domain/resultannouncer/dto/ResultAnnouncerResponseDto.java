package com.lotto.domain.resultannouncer.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ResultAnnouncerResponseDto(
        ResponseDto responseDto,
        String message) implements Serializable {
}
