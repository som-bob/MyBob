package com.my.bob.core.domain.member.exception;

public class DuplicateUserException extends Throwable {
    public DuplicateUserException(String msg) {
        super(msg);
    }
}
