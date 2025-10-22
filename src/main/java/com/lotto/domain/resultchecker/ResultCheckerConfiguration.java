package com.lotto.domain.resultchecker;

import com.lotto.domain.numbergenerator.NumberGeneratorFacade;
import com.lotto.domain.numberreceiver.NumberReceiverFacade;

class ResultCheckerConfiguration {

    public static ResultCheckerFacade resultCheckerFacade(NumberReceiverFacade numberReceiverFacade, NumberGeneratorFacade numberGeneratorFacade, TicketRepository ticketRepository) {
        WinnersRetriever winnersRetriever = new WinnersRetriever();
        return new ResultCheckerFacade(numberReceiverFacade,numberGeneratorFacade,ticketRepository, winnersRetriever);
    }
}
