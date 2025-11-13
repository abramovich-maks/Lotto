package com.lotto.infrastructure.resultannouncer.controller;

import com.lotto.domain.resultannouncer.ResultAnnouncerFacade;
import com.lotto.domain.resultannouncer.TicketListByUser;
import com.lotto.domain.resultannouncer.dto.ResultAnnouncerResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
class ResultAnnouncerRestController {

    private final ResultAnnouncerFacade resultAnnouncerFacade;

    @GetMapping("/result/{ticketId}")
    public ResponseEntity<ResultAnnouncerResponseDto> checkResult(@PathVariable String ticketId) {
        ResultAnnouncerResponseDto checkResult = resultAnnouncerFacade.checkResult(ticketId);
        return ResponseEntity.ok(checkResult);
    }

    @GetMapping("/result")
    public ResponseEntity<List<TicketListByUser>> checkListTicket() {
        List<TicketListByUser> listAllTicketByUser = resultAnnouncerFacade.createListAllTicketByUser();
        return ResponseEntity.ok(listAllTicketByUser);
    }
}
