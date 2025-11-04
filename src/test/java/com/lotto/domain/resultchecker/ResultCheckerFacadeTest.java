package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.resultchecker.dto.TicketResultDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultCheckerFacadeTest {

    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);
    NumberGeneratorFacade numberGeneratorFacade = mock(NumberGeneratorFacade.class);


    ResultCheckerFacade resultCheckerFacade = ResultCheckerConfiguration.resultCheckerFacade(
            numberReceiverFacade,
            numberGeneratorFacade,
            new InMemoryTicketResultRepositoryTestIml()
    );

    @Test
    public void should_empty_list_when_winnings_numbers_is_empty() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(new WinningNumbersDto(Set.of(), drawDate));
        // when
        List<TicketResultDto> resultList = resultCheckerFacade.createResultList();
        // then
        assertThat(resultList).isEmpty();
    }

    @Test
    public void should_empty_list_when_not_tickets_by_draw_date() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(new WinningNumbersDto(Set.of(1, 2, 3, 4, 5, 6), drawDate));

        WinnersRetriever winnersRetriever = new WinnersRetriever();
        // when
        List<TicketResultDto> resultList = resultCheckerFacade.createResultList();
        winnersRetriever.retrieveWinners(null, Set.of(1, 2, 3, 4, 5, 6));
        // then
        assertThat(resultList.size()).isEqualTo(0);
        assertThat(resultList).isEmpty();
    }

    @Test
    public void should_retrieve_all_tickets_and_save_to_db() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(new WinningNumbersDto(Set.of(1, 2, 3, 4, 5, 6), drawDate));

        TicketDto t1 = new TicketDto("ticket-1", Set.of(1, 2, 3, 7, 8, 9), drawDate);
        TicketDto t2 = new TicketDto("ticket-2", Set.of(10, 20, 30, 40, 50, 60), drawDate);
        when(numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate)).thenReturn(List.of(t1, t2));
        // when
        List<TicketResultDto> saved = resultCheckerFacade.createResultList();
        // then
        assertThat(saved).hasSize(2);
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-1")).findFirst().get().matches()).isEqualTo(3);
    }

    @Test
    public void should_result_when_matches_at_one_to_six() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(new WinningNumbersDto(Set.of(1, 2, 3, 4, 5, 6), drawDate));

        TicketDto t0 = new TicketDto("ticket-0", Set.of(7, 8, 9, 10, 11, 12), drawDate);
        TicketDto t1 = new TicketDto("ticket-1", Set.of(6, 7, 8, 9, 10, 11), drawDate);
        TicketDto t2 = new TicketDto("ticket-2", Set.of(5, 6, 7, 8, 9, 10), drawDate);
        TicketDto t3 = new TicketDto("ticket-3", Set.of(4, 5, 6, 7, 8, 9), drawDate);
        TicketDto t4 = new TicketDto("ticket-4", Set.of(3, 4, 5, 6, 7, 8), drawDate);
        TicketDto t5 = new TicketDto("ticket-5", Set.of(2, 3, 4, 5, 6, 7), drawDate);
        TicketDto t6 = new TicketDto("ticket-6", Set.of(1, 2, 3, 4, 5, 6), drawDate);
        when(numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate)).thenReturn(List.of(t0, t1, t2, t3, t4, t5, t6));
        // when
        List<TicketResultDto> saved = resultCheckerFacade.createResultList();
        // then
        assertThat(saved).hasSize(7);
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-0")).findFirst().get().resultCategory()).isEqualTo("NO_MATCH");
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-1")).findFirst().get().resultCategory()).isEqualTo("ONE_MATCH");
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-2")).findFirst().get().resultCategory()).isEqualTo("TWO_MATCH");
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-3")).findFirst().get().resultCategory()).isEqualTo("THREE_MATCH");
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-4")).findFirst().get().resultCategory()).isEqualTo("FOUR_MATCH");
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-5")).findFirst().get().resultCategory()).isEqualTo("FIVE_MATCH");
        assertThat(saved.stream().filter(result -> result.hash().equals("ticket-6")).findFirst().get().resultCategory()).isEqualTo("JACKPOT");
    }

    @Test
    public void should_return_ticket_by_id() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(new WinningNumbersDto(Set.of(1, 2, 3, 4, 5, 6), drawDate));

        TicketDto t0 = new TicketDto("ticket-0", Set.of(7, 8, 9, 10, 11, 12), drawDate);
        TicketDto t1 = new TicketDto("ticket-1", Set.of(6, 7, 8, 9, 10, 11), drawDate);
        when(numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate)).thenReturn(List.of(t0, t1));
        // when
        List<TicketResultDto> resultList = resultCheckerFacade.createResultList();
        TicketResultDto byTicketId = resultCheckerFacade.findByTicketId("ticket-1");
        // then
        assertThat(resultList.size()).isEqualTo(2);
        assertThat(byTicketId.hash()).isEqualTo("ticket-1");
        assertThat(byTicketId.drawDate()).isEqualTo(drawDate);
        assertThat(byTicketId.userNumbers()).isEqualTo(Set.of(6, 7, 8, 9, 10, 11));
        assertThat(byTicketId.winningNumbers()).isEqualTo(Set.of(1, 2, 3, 4, 5, 6));
        assertThat(byTicketId.matches()).isEqualTo(1);
        assertThat(byTicketId.resultCategory()).isEqualTo("ONE_MATCH");
    }

    @Test
    public void should_throw_an_exception_when_ticket_not_found() {
        // given
        LocalDateTime drawDate = LocalDateTime.of(2025, 10, 25, 12, 0);
        when(numberGeneratorFacade.generateWinningNumbers()).thenReturn(new WinningNumbersDto(Set.of(1, 2, 3, 4, 5, 6), drawDate));

        TicketDto t0 = new TicketDto("ticket-0", Set.of(7, 8, 9, 10, 11, 12), drawDate);
        TicketDto t1 = new TicketDto("ticket-1", Set.of(6, 7, 8, 9, 10, 11), drawDate);
        when(numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate)).thenReturn(List.of(t0, t1));
        // when
        resultCheckerFacade.createResultList();
        // then
        assertThrows(TicketNotFoundException.class, () -> resultCheckerFacade.findByTicketId("ticket-2"));
    }
}