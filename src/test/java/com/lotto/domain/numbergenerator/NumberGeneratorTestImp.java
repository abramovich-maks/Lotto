package com.lotto.domain.numbergenerator;

import java.util.Set;

class NumberGeneratorTestImp implements RandomNumberGenerable {

    private final Set<Integer> generatedNumbers;

    NumberGeneratorTestImp() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    NumberGeneratorTestImp(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    @Override
    public SixRandomNumbersDto generateSixRandomNumbers() {
        return SixRandomNumbersDto.builder()
                .numbers(generatedNumbers)
                .build();
    }
}
