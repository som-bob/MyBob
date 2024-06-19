package com.my.bob.handler;

import com.my.bob.common.dto.CommonResponse;
import com.my.bob.exception.BadRequestException;
import com.my.bob.exception.NonExistentUserException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Dto validate handler
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public CommonResponse handle(MethodArgumentNotValidException e, WebRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setError(HttpStatus.BAD_REQUEST, e.getFieldErrors());
        return commonResponse;
    }

    // LoginService, UserDetailService
    @ExceptionHandler(value = {
            BadCredentialsException.class,
            BadRequestException.class,
            NonExistentUserException.class,
            UsernameNotFoundException.class})
    public CommonResponse handle(BadCredentialsException e, WebRequest request) {
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setError(HttpStatus.BAD_REQUEST, e.getMessage());
        return commonResponse;
    }



}
