package com.lotto.domain.numberreceiver;

import java.util.Set;

class NumberValidator {

    private static final int COUNT_NUMBERS_FROM_USER = 6;
    private static final int MIN_NUMBER_FROM_USER = 1;
    private static final int MAX_NUMBER_FROM_USER = 99;

    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.size() == COUNT_NUMBERS_FROM_USER &&
                numbersFromUser.stream()
                        .allMatch(number -> number >= MIN_NUMBER_FROM_USER && number <= MAX_NUMBER_FROM_USER);
    }
}
