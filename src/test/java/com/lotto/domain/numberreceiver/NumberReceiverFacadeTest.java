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
    ZonedDateTime zonedDateTime = ZonedDateTime.of(2025, 10, 18, 12, 0, 0, 0, POLAND);
    AdjustableClock clock = new AdjustableClock(zonedDateTime.toInstant(), POLAND);


    NumberReceiverFacade numberReceiverFacade = NumberReceiverConfiguration.numberReceiverFacade(
            new InMemoryTicketRepositoryTestImpl(),
            clock,
            new HashGeneratorTestImpl()
    );

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
        LocalDateTime drowDate = LocalDateTime.of(2025, 10, 25, 12, 0, 0);
        // when
        List<TicketDto> ticketDtos = numberReceiverFacade.retrieveAllTicketsByDrawDate(drowDate);
        // then
        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .hash(result.ticketDto().hash())
                        .numbers(result.ticketDto().numbers())
                        .drawDate(drowDate)
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

    @Test
    void should_return_next_saturday_if_now_is_exactly_12() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        // then
        assertThat(result.ticketDto().drawDate()).isEqualTo(LocalDateTime.of(2025, 10, 25, 12, 0));
    }

    @Test
    void should_store_multiple_tickets_for_same_draw_date() {
        // given
        Set<Integer> numbers1 = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> numbers2 = Set.of(7, 8, 9, 10, 11, 12);
        // when
        InputNumberResultDto r1 = numberReceiverFacade.inputNumbers(numbers1);
        InputNumberResultDto r2 = numberReceiverFacade.inputNumbers(numbers2);
        LocalDateTime drawDate = r1.ticketDto().drawDate();
        List<TicketDto> tickets = numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate);

        // then
        assertThat(tickets).hasSize(2);
        assertThat(tickets).extracting(TicketDto::hash)
                .containsExactly(r1.ticketDto().hash(), r2.ticketDto().hash());
    }

    @Test
    public void it_should_return_next_draw_date() {
        // given
        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2025, 10, 25, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }
}