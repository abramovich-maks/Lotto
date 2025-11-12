package com.lotto.domain.resultannouncer;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketListByUser(
        String hash,
        Set<Integer> numbers,
        LocalDateTime drawDate
) {
}
