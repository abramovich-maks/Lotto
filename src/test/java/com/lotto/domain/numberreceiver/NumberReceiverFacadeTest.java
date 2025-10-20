package com.lotto.domain.numberreceiver;

import com.lotto.domain.AdjustableClock;
import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class NumberReceiverFacadeTest {

    private static final ZoneId POLAND = ZoneId.of("Europe/Warsaw");
    ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 10, 19, 12, 0, 0, 0, POLAND);
    AdjustableClock clock = new AdjustableClock(zonedDateTime.toInstant(), POLAND);


    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(
            new NumberValidator(),
            new InMemoryNumberReceiverRepositoryTestImpl(),
            clock);

    @Test
    public void should_return_success_when_user_gave_six_unique_numbers_in_range() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    public void should_return_save_to_database_when_user_gave_six_unique_numbers_in_range() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drowDate = LocalDateTime.of(2025, 10, 19, 12, 0, 0);
        // when
        List<TicketDto> ticketDtos = numberReceiverFacade.userNumbers(drowDate);
        // then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .ticketId(result.ticketId())
                        .drawDate(drowDate)
                        .numbersFromUser(result.numbersFromUser())
                        .build()
        );
    }

    @Test
    public void should_return_failed_when_user_gave_less_than_six_unique_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 5, 6);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_at_least_one_number_is_out_of_range() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5, 6);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("failed");
    }
}