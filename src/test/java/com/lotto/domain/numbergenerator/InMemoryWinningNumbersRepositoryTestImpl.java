package com.lotto.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class InMemoryWinningNumbersRepositoryTestImpl implements WinningNumbersRepository {

    Map<LocalDateTime, WinningNumbers> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public Optional<WinningNumbers> findNumbersByDate(final LocalDateTime date) {
        return Optional.ofNullable(inMemoryDatabase.get(date));
    }

    @Override
    public WinningNumbers save(final WinningNumbers winningNumbers) {
        inMemoryDatabase.put(winningNumbers.date(), winningNumbers);
        return winningNumbers;
    }
}
