package com.solution.exception;

public class AccountNotFoundException extends RuntimeException {
    public static final String NOT_FOUND_BY_ACCOUNT_NUMBER = "Account not found by this accountNumber %s";

    public AccountNotFoundException() {
    }
    public AccountNotFoundException(String message) {
        super(message);
    }
}
