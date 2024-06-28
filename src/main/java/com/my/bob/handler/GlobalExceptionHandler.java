package com.my.bob.handler;

import com.my.bob.common.dto.ResponseEntity;
import com.my.bob.exception.BadRequestException;
import com.my.bob.exception.NonExistentUserException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Dto validate handler
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity handle(MethodArgumentNotValidException e, WebRequest request) {
        exceptionLogging(e, request);

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setError(HttpStatus.BAD_REQUEST, e.getFieldErrors());
        return responseEntity;
    }

    // LoginService, UserDetailService
    @ExceptionHandler(value = {
            BadCredentialsException.class,
            BadRequestException.class,
            NonExistentUserException.class,
            UsernameNotFoundException.class})
    public ResponseEntity handle(BadCredentialsException e, WebRequest request) {
        exceptionLogging(e, request);

        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setError(HttpStatus.BAD_REQUEST, e.getMessage());
        return responseEntity;
    }

    // 모든 exception 발생시, 로깅
    private void exceptionLogging(Exception e, WebRequest request) {
        try {
            log.error("exception: {}", e.getClass().getName());

            String description = request.getDescription(true);
            if(StringUtils.isEmpty(description)) {
                return;
            }

            String[] descriptionSplit = description.split(";");
            for (String descriptionStr : descriptionSplit) {
                log.error("Request description: {}", descriptionStr);
            }
        } catch (Exception ex) { /* do nothing */ }
    }

}
