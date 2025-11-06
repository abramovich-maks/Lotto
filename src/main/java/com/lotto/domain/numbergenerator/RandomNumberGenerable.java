package com.lotto.domain.numbergenerator;

public interface RandomNumberGenerable {

    SixRandomNumbersDto generateSixRandomNumbers(final int count,final int lowerBand, final int upperBound );
}
