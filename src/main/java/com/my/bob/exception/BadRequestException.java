package com.my.bob.exception;

public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
    // 해당 Exception 발생할 경우, HTTP_STATUS.BAD_REQUEST 반환
}
