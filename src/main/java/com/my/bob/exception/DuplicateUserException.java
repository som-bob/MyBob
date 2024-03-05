package com.my.bob.exception;

public class DuplicateUserException extends Throwable {
    public DuplicateUserException(String msg) {
        super(msg);
    }
}
