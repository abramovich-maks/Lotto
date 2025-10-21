package com.lotto.domain.numbergenerator;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class RandomGenerator implements RandomNumberGenerable {

    private static final int LOWER_BOUND = 1;
    private static final int UPPER_BOUND = 99;
    private static final int NUMBERS_COUNT = 6;

    private final Random random = new SecureRandom();

    @Override
    public Set<Integer> generateSixRandomNumbers() {
        Set<Integer> winningNumbers = new HashSet<>();
        while (winningNumbers.size() < NUMBERS_COUNT) {
            int randomNumber = random.nextInt(UPPER_BOUND - LOWER_BOUND + 1) + LOWER_BOUND;
            winningNumbers.add(randomNumber);
        }
        return winningNumbers;
    }
}