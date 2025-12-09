package com.lotto.domain.numberreceiver;

import com.lotto.domain.drowdate.DrawDateFacade;
import com.lotto.domain.loginandregister.User;
import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NumberReceiverFacadeTest {

    DrawDateFacade drawDateFacade = mock(DrawDateFacade.class);


    NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().numberReceiverFacade(
            new InMemoryTicketRepositoryTestImpl(),
            drawDateFacade,
            new HashGenerator()
    );

    @Test
    public void should_return_success_when_user_gave_six_unique_numbers_in_range() {
        // given
        initializeMockUser("user-1");
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers("user-1",numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("success");
    }

    @Test
    @WithMockUser()
    public void should_return_save_to_database_when_user_gave_six_unique_numbers_in_range() {
        // given
        initializeMockUser("user-1");
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        InputNumberResultDto result = numberReceiverFacade.inputNumbers("user-1",numbersFromUser);
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
        InputNumberResultDto result = numberReceiverFacade.inputNumbers("user-1",numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_user_gave_more_than_six_numbers() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers("user-1",numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    public void should_return_failed_when_at_least_one_number_is_out_of_range() {
        // given
        Set<Integer> numbersFromUser = Set.of(1, 200, 3, 4, 5, 6);
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers("user-1",numbersFromUser);
        // then
        assertThat(result.message()).isEqualTo("failed");
    }

    @Test
    void should_return_next_saturday_if_now_is_exactly_12() {
        // given
        initializeMockUser("user-1");
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        // when
        InputNumberResultDto result = numberReceiverFacade.inputNumbers("user-1",numbersFromUser);
        // then
        assertThat(result.ticketDto().drawDate()).isEqualTo(LocalDateTime.of(2025, 10, 25, 12, 0));
    }

    @Test
    void should_store_multiple_tickets_for_same_draw_date() {
        // given
        initializeMockUser("user-1");
        Set<Integer> numbersFromUser1 = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> numbersFromUser2 = Set.of(7, 8, 9, 10, 11, 12);
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        // when
        InputNumberResultDto result1 = numberReceiverFacade.inputNumbers("user-1",numbersFromUser1);
        InputNumberResultDto result2 = numberReceiverFacade.inputNumbers("user-2",numbersFromUser2);
        LocalDateTime drawDate = result1.ticketDto().drawDate();
        List<TicketDto> tickets = numberReceiverFacade.retrieveAllTicketsByDrawDate(drawDate);

        // then
        assertThat(tickets).hasSize(2);
        assertThat(tickets).extracting(TicketDto::hash)
                .containsExactlyInAnyOrder(result1.ticketDto().hash(), result2.ticketDto().hash());

    }

    @Test
    public void should_return_next_draw_date() {
        // given
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        // when
        LocalDateTime testedDrawDate = numberReceiverFacade.retrieveNextDrawDate();
        // then
        LocalDateTime expectedDrawDate = LocalDateTime.of(2025, 10, 25, 12, 0, 0);
        assertThat(testedDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_all_tickets_for_different_users_separately() {
        // User 1
        // given
        initializeMockUser("user-1");
        Set<Integer> ticketNumbersUser1_1 = Set.of(1, 2, 3, 4, 5, 6);
        Set<Integer> ticketNumbersUser1_2 = Set.of(10, 20, 30, 40, 50, 60);
        Set<Integer> ticketNumbersUser1_3 = Set.of(19, 29, 39, 49, 59, 69);
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        // when
        InputNumberResultDto user1Result1 = numberReceiverFacade.inputNumbers("user-1",ticketNumbersUser1_1);
        InputNumberResultDto user1Result2 = numberReceiverFacade.inputNumbers("user-1",ticketNumbersUser1_2);
        InputNumberResultDto user1Result3 = numberReceiverFacade.inputNumbers("user-1",ticketNumbersUser1_3);
        // then
        List<TicketDto> ticketsUser1 = numberReceiverFacade.retrieveAllTicketsByUsername("user-1");
        assertThat(ticketsUser1).hasSize(3);
        assertThat(ticketsUser1)
                .extracting(TicketDto::hash)
                .containsExactlyInAnyOrder(
                        user1Result1.ticketDto().hash(),
                        user1Result2.ticketDto().hash(),
                        user1Result3.ticketDto().hash()
                );
        // User 2
        // given
        initializeMockUser("user-2");
        Set<Integer> ticketNumbersUser2_1 = Set.of(1, 2, 3, 4, 5, 6);
        when(drawDateFacade.getNextDrawDate()).thenReturn(LocalDateTime.of(2025, 10, 25, 12, 0, 0));
        // when
        InputNumberResultDto user2Result1 = numberReceiverFacade.inputNumbers("user-2",ticketNumbersUser2_1);
        // then
        List<TicketDto> ticketsUser2 = numberReceiverFacade.retrieveAllTicketsByUsername("user-2");
        assertThat(ticketsUser2).hasSize(1);
        assertThat(ticketsUser2)
                .extracting(TicketDto::hash)
                .containsExactlyInAnyOrder(
                        user2Result1.ticketDto().hash()
                );
    }

    private void initializeMockUser(String userEmail) {
        User mockUser = new User(userEmail, "password", "token", Set.of("role"));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);
    }
}