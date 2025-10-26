package com.lotto.domain.numberreceiver;


import com.lotto.domain.drowdate.DrawDateFacade;

class NumberReceiverConfiguration {

    public static NumberReceiverFacade numberReceiverFacade(final TicketRepository repository,
                                                            final DrawDateFacade drawDateFacade,
                                                            final HashGenerable hashGenerator) {
        NumberValidator validator = new NumberValidator();
        return new NumberReceiverFacade(validator, hashGenerator, repository, drawDateFacade);
    }
}
