package com.lotto.domain.resultannouncer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record ResultResponse(
        String hash,
        LocalDateTime drawDate,
        Set<Integer> userNumbers,
        Set<Integer> winningNumbers,
        int matches,
        String resultCategory
) {
}
