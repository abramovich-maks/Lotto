package com.lotto.domain.numberreceiver.dto;

import lombok.Builder;

@Builder
public record InputNumberResultDto(TicketDto ticketDto, String message) {
}
