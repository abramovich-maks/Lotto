package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultannouncer.dto.ResponseDto;
import com.lotto.domain.resultchecker.dto.TicketResultDto;

class ResultMapper {

    static ResponseDto mapFromResultResponseToResponseDto(final ResultResponse response) {
        return ResponseDto.builder()
                .hash(response.hash())
                .drawDate(response.drawDate())
                .userNumbers(response.userNumbers())
                .winningNumbers(response.winningNumbers())
                .matches(response.matches())
                .resultCategory(response.resultCategory())
                .build();
    }

    static ResultResponse mapFromTicketResultDtoToResultResponse(final TicketResultDto byTicketId) {
        return ResultResponse.builder()
                .hash(byTicketId.hash())
                .drawDate(byTicketId.drawDate())
                .userNumbers(byTicketId.userNumbers())
                .winningNumbers(byTicketId.winningNumbers())
                .matches(byTicketId.matches())
                .resultCategory(byTicketId.resultCategory())
                .build();
    }
}