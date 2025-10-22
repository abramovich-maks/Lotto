package com.lotto.domain.resultchecker.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketResultDto(
        String hash,
        LocalDateTime drawDate,
        Set<Integer> userNumbers,
        Set<Integer> winningNumbers,
        Set<Integer> matchedNumbers,
        int matches,
        String resultCategory
) {
}
