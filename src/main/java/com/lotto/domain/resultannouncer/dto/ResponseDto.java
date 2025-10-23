package com.lotto.domain.resultannouncer.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record ResponseDto(
        String hash,
        LocalDateTime drawDate,
        Set<Integer> userNumbers,
        Set<Integer> winningNumbers,
        int matches,
        String resultCategory
) {
}
