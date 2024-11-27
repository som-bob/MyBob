package com.my.bob.v1.member.exception;

public class NonExistentUserException extends UserLoginException {
    public NonExistentUserException(String message) {
        super(message);
    }
}
