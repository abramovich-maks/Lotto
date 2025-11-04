package com.lotto.domain.resultchecker;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Document
record TicketResult(
        @Id
        String hash,
        LocalDateTime drawDate,
        Set<Integer> userNumbers,
        Set<Integer> winningNumbers,
        Set<Integer> matchedNumbers,
        int matches,
        String resultCategory
) {
}
