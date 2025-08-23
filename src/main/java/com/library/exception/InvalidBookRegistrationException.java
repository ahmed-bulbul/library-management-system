package com.library.exception;

public class InvalidBookRegistrationException extends RuntimeException {
    public InvalidBookRegistrationException(String message) {
        super(message);
    }
}
