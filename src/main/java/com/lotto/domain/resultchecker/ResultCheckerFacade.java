package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.TicketResultDto;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
class ResultCheckerFacade {

    private final NumberReceiverFacade numberReceiver;
    private final NumberGeneratorFacade numberGenerator;
    private final TicketRepository ticketRepository;
    private final WinnersRetriever winnersRetriever;

    public List<TicketResult> createResultList() {
        WinningNumbersDto winningNumbersDto = numberGenerator.generateWinningNumbers();
        Set<Integer> winningNumbers = winningNumbersDto.winningNumbers();
        if (winningNumbers == null || winningNumbers.isEmpty()) {
            return Collections.emptyList();
        }
        List<TicketDto> allTicketsByDate = numberReceiver.retrieveAllTicketsByDrawDate(winningNumbersDto.date());
        List<Ticket> tickets = ResultCheckerMapper.mapFromTicketDto(allTicketsByDate);

        List<TicketResult> results = winnersRetriever.retrieveWinners(tickets, winningNumbers);
        List<TicketResult> saved = ticketRepository.saveAll(results);
        return saved;
    }

    public TicketResultDto findByTicketId(String ticketId) {
        TicketResult ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with id: " + ticketId + " not found"));
        return TicketResultDto.builder()
                .hash(ticket.hash())
                .drawDate(ticket.drawDate())
                .userNumbers(ticket.userNumbers())
                .winningNumbers(ticket.winningNumbers())
                .matchedNumbers(ticket.matchedNumbers())
                .matches(ticket.matches())
                .resultCategory(ticket.resultCategory())
                .build();
    }
}
