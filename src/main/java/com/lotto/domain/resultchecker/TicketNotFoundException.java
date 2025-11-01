package com.lotto.domain.resultchecker;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(final String message) {
        super(message);
    }
}
