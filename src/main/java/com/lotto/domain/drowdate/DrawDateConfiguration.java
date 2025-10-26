package com.lotto.domain.drowdate;

import java.time.Clock;

class DrawDateConfiguration {

    public static DrawDateFacade drawDateFacade(Clock clock) {
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new DrawDateFacade(drawDateGenerator);
    }
}
