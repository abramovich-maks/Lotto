package com.lotto.domain.resultchecker;

import com.lotto.domain.numberreceiver.dto.TicketDto;

import java.util.List;

class ResultCheckerMapper {

    static List<Ticket> mapFromTicketDto(List<TicketDto> allTicketsByDate) {
        return allTicketsByDate.stream()
                .map(ticketDto -> Ticket.builder()
                        .drawDate(ticketDto.drawDate())
                        .hash(ticketDto.hash())
                        .numbers(ticketDto.numbers())
                        .build())
                .toList();
    }
}