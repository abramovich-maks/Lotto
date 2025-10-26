package com.lotto.domain.numbergenerator;

import com.lotto.domain.drowdate.DrawDateFacade;
import com.lotto.domain.numbergenerator.dto.WinningNumbersDto;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class NumberGeneratorFacade {

    private final DrawDateFacade drawDateFacade;
    private final RandomNumberGenerable randomGenerable;
    private final WinningNumbersRepository winningNumbersRepository;
    private WinningNumberValidator winningNumberValidator;

    public WinningNumbersDto generateWinningNumbers() {
        LocalDateTime nextDrawDate = drawDateFacade.getNextDrawDate();
        SixRandomNumbersDto dto = randomGenerable.generateSixRandomNumbers();
        Set<Integer> winningNumbers = dto.numbers();
        winningNumberValidator.validate(winningNumbers);
        winningNumbersRepository.save(WinningNumbers.builder()
                .winningNumbers(winningNumbers)
                .date(nextDrawDate)
                .build());
        return  WinningNumbersDto.builder()
                .winningNumbers(winningNumbers)
                .date(nextDrawDate)
                .build();
    }

    public WinningNumbersDto retrieveWinningNumberByDate(LocalDateTime date) {
        WinningNumbers numbersByDate = winningNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new WinningNumbersNotFoundException("Not Found"));
        return WinningNumbersDto.builder()
                .winningNumbers(numbersByDate.winningNumbers())
                .date(numbersByDate.date())
                .build();
    }
}
