package com.lotto.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Optional;

interface WinningNumbersRepository {

    Optional<WinningNumbers> findNumbersByDate(LocalDateTime date);

    WinningNumbers save(WinningNumbers winningNumbers);

}
