package com.my.bob.exception;

public class NonExistentUserException extends UserLoginException {
    public NonExistentUserException(String message) {
        super(message);
    }
}
