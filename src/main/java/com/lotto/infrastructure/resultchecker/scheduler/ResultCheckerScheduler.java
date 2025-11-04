package com.lotto.infrastructure.resultchecker.scheduler;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.resultchecker.ResultCheckerFacade;
import com.lotto.domain.resultchecker.dto.TicketResultDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log4j2
@Component
public class ResultCheckerScheduler {

    private final ResultCheckerFacade resultCheckerFacade;
    private final NumberGeneratorFacade numberGeneratorFacade;

    @Scheduled(cron = "${lotto.tickets-results.occurrence}")
    public List<TicketResultDto> checkTicketsResults() {
        if (!numberGeneratorFacade.areWinningNumbersGeneratedByDate()) {
            throw new RuntimeException("Winning number are not generated");
        }

        List<TicketResultDto> ticketResultList = resultCheckerFacade.createResultList();

        List<TicketResultDto> jackpotTickets = getTicketsByCategory(ticketResultList, "JACKPOT");
        List<TicketResultDto> fiveMatchTickets = getTicketsByCategory(ticketResultList, "FIVE_MATCH");
        List<TicketResultDto> fourMatchTickets = getTicketsByCategory(ticketResultList, "FOUR_MATCH");

        if (!jackpotTickets.isEmpty()) {
            log.info("Have JACKPOT WINNERS!!! {}", formatHashes(jackpotTickets));
        }
        if (!fiveMatchTickets.isEmpty()) {
            log.info("Five match winners!!! {}", formatHashes(fiveMatchTickets));
        }
        if (!fourMatchTickets.isEmpty()) {
            log.info("Four match winners!!! {}", formatHashes(fourMatchTickets));
        }
        return ticketResultList;
    }

    private List<TicketResultDto> getTicketsByCategory(List<TicketResultDto> tickets, String category) {
        return tickets.stream()
                .filter(ticket -> ticket.resultCategory().equals(category))
                .toList();
    }

    private String formatHashes(List<TicketResultDto> tickets) {
        return tickets.stream()
                .map(TicketResultDto::hash)
                .collect(Collectors.joining(", "));
    }
}
