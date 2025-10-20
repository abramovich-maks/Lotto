package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;
    private final TicketRepository repository;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (!areAllNumbersInRange) {
            return InputNumberResultDto.builder()
                    .message("failed")
                    .build();
        }
        String hashTicket = hashGenerator.getHash();
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        Ticket savedTicket = repository.save(new Ticket(hashTicket, numbersFromUser, drawDate));
        return InputNumberResultDto.builder()
                .ticketDto(TicketMapper.mapFromTicket(savedTicket))
                .message("success")
                .build();
    }

    public List<TicketDto> retrieveAllTicketsByDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return repository.findAllTicketsByDrawDate(date)
                .stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }
}
