package com.lotto.domain.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TicketIsExistDto(String hash, LocalDateTime drawDate) {
}
