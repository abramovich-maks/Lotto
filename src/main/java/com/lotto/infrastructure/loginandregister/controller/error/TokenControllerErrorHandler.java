package com.lotto.infrastructure.loginandregister.controller.error;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class TokenControllerErrorHandler {

    private static final String BAD_CREDENTIALS = "Bad credentials";

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public LoginErrorResponse handleBadCredentials() {
        return new LoginErrorResponse(BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
    }
}
