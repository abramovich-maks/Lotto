package com.lotto.domain.resultchecker;

class TicketNotFoundException extends RuntimeException {
    TicketNotFoundException(final String message) {
        super(message);
    }
}
