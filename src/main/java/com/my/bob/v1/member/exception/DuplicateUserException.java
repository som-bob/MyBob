package com.my.bob.v1.member.exception;

public class DuplicateUserException extends Throwable {
    public DuplicateUserException(String msg) {
        super(msg);
    }
}
