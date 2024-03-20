package com.my.bob.exception;

public class NonExistentUserException extends Exception{
    public NonExistentUserException(String message) {
        super(message);
    }
}
