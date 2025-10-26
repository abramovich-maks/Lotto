package com.lotto.domain.numbergenerator;

import com.lotto.domain.drowdate.DrawDateFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;

class NumberGeneratorConfiguration {

    public static NumberGeneratorFacade numberGeneratorFacade(RandomNumberGenerable generator, DrawDateFacade drawDateFacade, WinningNumbersRepository winningNumbersRepository) {
        WinningNumberValidator winningNumberValidator = new WinningNumberValidator();
        return new NumberGeneratorFacade(drawDateFacade,
                generator,
                winningNumbersRepository,
                winningNumberValidator);
    }
}
