package com.lotto.domain.numbergenerator;

public interface RandomNumberGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(final int lowerBand, final int upperBound, final int count);
}
