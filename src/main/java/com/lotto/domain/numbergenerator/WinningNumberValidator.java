package com.lotto.domain.numbergenerator;

import java.util.Set;

class WinningNumberValidator {


    Set<Integer> validate(final Set<Integer> winningNumbers) {
        boolean outOfRange = winningNumbers.stream()
                .anyMatch(number -> number < 1 || number > 99);
        if (outOfRange) {
            throw new IllegalStateException("Number out of range!");
        }
        return winningNumbers;
    }
}
