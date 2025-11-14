package com.lotto.domain.resultannouncer;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultannouncer.dto.ResponseDto;
import com.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.TicketResultDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.lotto.domain.resultannouncer.ResultMapper.mapFromResultResponseToResponseDto;

@AllArgsConstructor
public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;
    private final AnnouncerRepository announcerRepository;
    private final NumberReceiverFacade numberReceiverFacade;
    private final Clock clock;

    @Cacheable(value = "resultAnnounce")
    public ResultAnnouncerResponseDto checkResult(String hash) {
        if (hash == null || hash.isBlank()) {
            return ResultAnnouncerResponseDto.builder()
                    .responseDto(null)
                    .message("Invalid hash")
                    .build();
        }
        if (announcerRepository.existsById(hash)) {
            Optional<ResultResponse> resultResponseCached = announcerRepository.findById(hash);
            if (resultResponseCached.isPresent()) {
                return new ResultAnnouncerResponseDto(mapFromResultResponseToResponseDto(resultResponseCached.get()), "Result retrieved from cache");
            }
        }
        TicketResultDto byTicketId = resultCheckerFacade.findByTicketId(hash);
        if (byTicketId == null) {
            return ResultAnnouncerResponseDto.builder()
                    .responseDto(null)
                    .message("Hash does not exist")
                    .build();
        }
        ResponseDto responseDto = buildResponseDto(byTicketId);
        announcerRepository.save(buildResponse(responseDto, LocalDateTime.now(clock)));
        if (announcerRepository.existsById(hash) && !isAfterResultAnnouncementTime(byTicketId)) {
            return new ResultAnnouncerResponseDto(responseDto, "Result not announced yet");
        }
        return ResultAnnouncerResponseDto.builder()
                .responseDto(responseDto)
                .message("Result available")
                .build();
    }

    @Cacheable(value = "listTickets")
    public List<TicketListByUser> createListAllTicketByUser() {
        List<TicketDto> ticketDtos = numberReceiverFacade.retrieveAllTicketsByUsername();
        return ticketDtos.stream().map(ticketDto -> TicketListByUser.builder()
                .hash(ticketDto.hash())
                .numbers(ticketDto.numbers())
                .drawDate(ticketDto.drawDate())
                .build()).toList();
    }

    private static ResultResponse buildResponse(ResponseDto responseDto, LocalDateTime now) {
        return ResultResponse.builder()
                .hash(responseDto.hash())
                .drawDate(responseDto.drawDate())
                .userNumbers(responseDto.userNumbers())
                .winningNumbers(responseDto.winningNumbers())
                .matches(responseDto.matches())
                .resultCategory(responseDto.resultCategory())
                .createdDate(now)
                .build();
    }

    private static ResponseDto buildResponseDto(TicketResultDto resultDto) {
        return ResponseDto.builder()
                .hash(resultDto.hash())
                .drawDate(resultDto.drawDate())
                .userNumbers(resultDto.userNumbers())
                .winningNumbers(resultDto.winningNumbers())
                .matches(resultDto.matches())
                .resultCategory(resultDto.resultCategory())
                .build();
    }

    private boolean isAfterResultAnnouncementTime(TicketResultDto resultDto) {
        LocalDateTime announcementDateTime = resultDto.drawDate();
        return LocalDateTime.now(clock).isAfter(announcementDateTime);
    }
}