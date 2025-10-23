package com.lotto.domain.resultannouncer;

import com.lotto.domain.resultannouncer.dto.ResponseDto;
import com.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.TicketResultDto;
import lombok.AllArgsConstructor;

import java.util.Optional;

import static com.lotto.domain.resultannouncer.ResultMapper.mapFromTicketResultDtoToResultResponse;
import static com.lotto.domain.resultannouncer.ResultMapper.mapFromResultResponseToResponseDto;

@AllArgsConstructor
public class ResultAnnouncerFacade {

    private final ResultCheckerFacade resultCheckerFacade;
    private final AnnouncerRepository announcerRepository;

    public ResultAnnouncerResponseDto checkResult(String hash) {
        if (hash == null || hash.isBlank()) {
            return ResultAnnouncerResponseDto.builder()
                    .responseDto(null)
                    .message("Invalid hash")
                    .build();
        }
        if (announcerRepository.existsById(hash)) {
            Optional<ResultResponse> resultFromCashed = announcerRepository.findById(hash);
            if (resultFromCashed.isPresent()) {
                ResultResponse cached = resultFromCashed.get();
                ResponseDto responseDto = mapFromResultResponseToResponseDto(cached);
                return ResultAnnouncerResponseDto.builder()
                        .responseDto(responseDto)
                        .message("OK (from cache)")
                        .build();
            }
        }
        TicketResultDto byTicketId = resultCheckerFacade.findByTicketId(hash);
        if (byTicketId == null) {
            return ResultAnnouncerResponseDto.builder()
                    .responseDto(null)
                    .message("Hash does not exist")
                    .build();
        }
        ResultResponse responseToSave = mapFromTicketResultDtoToResultResponse(byTicketId);
        announcerRepository.save(responseToSave);
        ResponseDto responseDto = mapFromResultResponseToResponseDto(responseToSave);
        return ResultAnnouncerResponseDto.builder()
                .responseDto(responseDto)
                .message("OK")
                .build();
    }
}