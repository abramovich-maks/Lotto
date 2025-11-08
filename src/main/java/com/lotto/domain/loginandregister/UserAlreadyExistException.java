package com.lotto.domain.loginandregister;

public class UserAlreadyExistException extends RuntimeException {

    private final String userEmail;

    public UserAlreadyExistException(String userEmail) {
        super(String.format("User with email %s already exists",userEmail));
        this.userEmail = userEmail;
    }
}
