package com.my.bob.core.domain.member.exception;

import com.my.bob.core.exception.UserLoginException;

public class NonExistentUserException extends UserLoginException {
    public NonExistentUserException(String message) {
        super(message);
    }
}
