package com.lotto.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

public record Ticket(String ticketId, LocalDateTime drowDate, Set<Integer> numbersFromUser) {
}
