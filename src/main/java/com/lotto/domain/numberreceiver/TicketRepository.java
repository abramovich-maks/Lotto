package com.lotto.domain.numberreceiver;

import com.lotto.domain.numberreceiver.dto.TicketDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
interface TicketRepository extends MongoRepository<Ticket, String> {

    List<Ticket> findAllTicketsByDrawDate(LocalDateTime date);

    List<TicketDto> findAllTicketsByUserName(String userName);
}
