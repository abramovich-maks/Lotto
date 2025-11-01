package com.lotto.domain.resultchecker;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TicketResultRepository extends MongoRepository<TicketResult, String> {

    Optional<TicketResult> findById(String ticketId);
}
