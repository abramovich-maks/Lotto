package com.lotto.domain.numberreceiver;

import com.lotto.domain.drowdate.DrawDateFacade;
import com.lotto.domain.numberreceiver.dto.InputNumberResultDto;
import com.lotto.domain.numberreceiver.dto.TicketDto;
import com.lotto.domain.numberreceiver.dto.TicketIsExistDto;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final HashGenerable hashGenerator;
    private final TicketRepository repository;
    private final DrawDateFacade drawDateFacade;

    @CacheEvict(value = "listTickets", key = "#userName")
    public InputNumberResultDto inputNumbers(String userName, Set<Integer> numbersFromUser) {
        boolean areAllNumbersInRange = validator.areAllNumbersInRange(numbersFromUser);
        if (!areAllNumbersInRange) {
            return InputNumberResultDto.builder()
                    .message("failed")
                    .build();
        }
        String hashTicket = hashGenerator.getHash();
        LocalDateTime drawDate = drawDateFacade.getNextDrawDate();

        Ticket savedTicket = repository.save(new Ticket(hashTicket, numbersFromUser, drawDate, userName));
        return InputNumberResultDto.builder()
                .ticketDto(TicketMapper.mapFromTicket(savedTicket))
                .message("success")
                .build();
    }

    public List<TicketDto> retrieveAllTicketsByDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateFacade.getNextDrawDate();
        if (date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return repository.findAllTicketsByDrawDate(date)
                .stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

    @Cacheable(value = "listTickets", key = "#userName")
    public List<TicketDto> retrieveAllTicketsByUsername(String userName) {
        return repository.findAllTicketsByUserName(userName)
                .stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

    public LocalDateTime retrieveNextDrawDate() {
        return drawDateFacade.getNextDrawDate();
    }

    public boolean existsByHash(final String hash) {
        return repository.existsById(hash);
    }

    public TicketIsExistDto findByHash(final String hash) {
        Ticket byId = repository.findByHash(hash)
                .orElseThrow();
        return TicketIsExistDto.builder()
                .hash(byId.hash())
                .drawDate(byId.drawDate())
                .build();
    }
}
