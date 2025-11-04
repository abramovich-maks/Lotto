package com.lotto.domain.resultchecker;

import com.lotto.domain.resultchecker.dto.TicketResultDto;

import java.util.List;

class ResulCheckerMapper {

    public static List<TicketResultDto> mapFromTicketResultToTicketResultDto(final List<TicketResult> saved) {
        return saved.stream().map(ticketDto -> TicketResultDto.builder()
                .hash(ticketDto.hash())
                .drawDate(ticketDto.drawDate())
                .userNumbers(ticketDto.userNumbers())
                .winningNumbers(ticketDto.winningNumbers())
                .matchedNumbers(ticketDto.matchedNumbers())
                .matches(ticketDto.matches())
                .resultCategory(ticketDto.resultCategory())
                .build()).toList();
    }
}
