package com.lotto.domain.resultchecker;

import java.util.List;
import java.util.Optional;

interface TicketRepository {

    Optional<TicketResult> findById(String ticketId);

    List<TicketResult> saveAll(List<TicketResult> results);
}
