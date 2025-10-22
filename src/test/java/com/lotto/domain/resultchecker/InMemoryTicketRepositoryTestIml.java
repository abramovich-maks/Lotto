package com.lotto.domain.resultchecker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class InMemoryTicketRepositoryTestIml implements TicketRepository {

    Map<String, TicketResult> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<TicketResult> findById(final String ticketId) {
        return Optional.ofNullable(inMemoryDatabase.get(ticketId));
    }

    @Override
    public List<TicketResult> saveAll(final List<TicketResult> results) {
        if (results == null) {
            return List.of();
        }
        List<TicketResult> saved = results.stream()
                .map(result -> {
                    String key = result.hash();
                    inMemoryDatabase.put(key, result);
                    return result;
                })
                .collect(Collectors.toList());
        return saved;
    }
}
