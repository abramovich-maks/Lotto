package com.lotto.infrastructure.numbergenerator.scheduler;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Log4j2
@Component
class WinningNumbersScheduler {

    private final NumberGeneratorFacade numberGeneratorFacade;

    @Scheduled(cron = "${lotto.number-generator.lotteryRunOccurrence}")
    public WinningNumbersDto generateWinningNumbers() {
        WinningNumbersDto winningNumbersDto = numberGeneratorFacade.generateWinningNumbers();
      log.info(winningNumbersDto.winningNumbers());
      log.info(winningNumbersDto.date());
      return winningNumbersDto;
    }
}
