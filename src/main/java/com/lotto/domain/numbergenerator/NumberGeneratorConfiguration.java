package com.lotto.domain.numbergenerator;

import com.lotto.domain.numberreceiver.NumberReceiverFacade;

class NumberGeneratorConfiguration {

    public static NumberGeneratorFacade numberGeneratorFacade(RandomNumberGenerable generator, NumberReceiverFacade numberReceiverFacade, WinningNumbersRepository winningNumbersRepository) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new NumberGeneratorFacade(numberReceiverFacade,
                generator,
                winningNumbersRepository,
                winningNumberValidator);
    }
}
