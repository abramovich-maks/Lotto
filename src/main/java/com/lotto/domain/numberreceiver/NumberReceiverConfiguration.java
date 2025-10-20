package com.lotto.domain.numberreceiver;

import java.time.Clock;

class NumberReceiverConfiguration {

    public static NumberReceiverFacade numberReceiverFacade(final TicketRepository repository,
                                                            final Clock clock,
                                                            final HashGenerable hashGenerator) {
        NumberValidator validator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(validator, drawDateGenerator, hashGenerator, repository);
    }
}
