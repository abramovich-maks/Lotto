package com.lotto.domain.resultchecker;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record TicketResult(
        String hash,
        LocalDateTime drawDate,
        Set<Integer> userNumbers,
        Set<Integer> winningNumbers,
        Set<Integer> matchedNumbers,
        int matches,
        String resultCategory
) {
}
