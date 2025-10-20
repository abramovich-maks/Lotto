package com.lotto.domain.numberreceiver;

/*
Użytkownik podaje 6 liczb.
Liczby muszą być w zakresie od 1 do 99.
Liczby nie mogą się powtarzać.
Po poprawnym wysłaniu liczb użytkownik otrzymuje:
    informację, że liczby są poprawne,
    unikalny identyfikator losowania (userLotteryId),
    date losowania
*/

import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
class NumberReceiverFacade {

    private final NumberValidator validator;
    private final NumberReceiverRepository repository;
    private Clock clock;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (!areAllNumbersInRange) {
            return InputNumberResultDto.builder().message("failed").build();
        }
        String ticketId = UUID.randomUUID().toString();
        LocalDateTime drowDate = LocalDateTime.now(clock);
        Ticket savedTicket = repository.save(new Ticket(ticketId, drowDate, numbersFromUser));
        return InputNumberResultDto.builder()
                .message("success")
                .drawDate(savedTicket.drowDate())
                .numbersFromUser(numbersFromUser)
                .ticketId(savedTicket.ticketId())
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }
}
