package com.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(String hash, Set<Integer> numbers, LocalDateTime drawDate) implements Serializable {
}
