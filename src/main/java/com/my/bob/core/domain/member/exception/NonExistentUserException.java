package com.my.bob.core.domain.member.exception;

public class NonExistentUserException extends UserLoginException {
    public NonExistentUserException(String message) {
        super(message);
    }
}
