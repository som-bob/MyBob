package com.my.bob.core.config;

import com.my.bob.core.domain.base.dto.ResponseDto;
import com.my.bob.core.domain.member.exception.UserLoginException;
import com.my.bob.core.domain.recipe.exception.IngredientException;
import com.my.bob.core.domain.refrigerator.exception.RefrigeratorException;
import com.my.bob.core.exception.BadRequestException;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

import static com.my.bob.core.domain.base.dto.ResponseDto.FailCode;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Dto validate handler
    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto<Void>> handle(MethodArgumentNotValidException e, WebRequest request) {
        exceptionLogging(e, request);

        // 첫번째 에러 메세지만 return
        List<FieldError> fieldErrors = e.getFieldErrors();
        String defaultMessage = fieldErrors.get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.V_00001, defaultMessage));
    }

    // LoginService, UserDetailService
    @ExceptionHandler(value = {
            UserLoginException.class})
    public ResponseEntity<ResponseDto<Void>> handle(UserLoginException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.V_00001, e.getMessage()));
    }

    // LoginService, UserDetailService
    @ExceptionHandler(value = {
            UsernameNotFoundException.class})
    public ResponseEntity<ResponseDto<Void>> handle(UsernameNotFoundException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.I_00001, e.getMessage()));
    }

    // LoginService, UserDetailService
    @ExceptionHandler(value = {
            BadCredentialsException.class})
    public ResponseEntity<ResponseDto<Void>> handle(BadCredentialsException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.I_00001, e.getMessage()));
    }

    // BadRequestException
    // 해당 Exception 발생할 경우, HTTP_STATUS.BAD_REQUEST 반환
    @ExceptionHandler(value = {
            BadRequestException.class})
    public ResponseEntity<ResponseDto<Void>> handle(BadRequestException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.I_00001, e.getMessage()));
    }


    // IllegalArgumentException
    @ExceptionHandler(value = {
            IllegalArgumentException.class
    })
    public ResponseEntity<ResponseDto<Void>> handle(IllegalArgumentException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.I_00001, e.getMessage()));
    }

    // 냉장고 관련 Exception
    @ExceptionHandler(value = {
            RefrigeratorException.class
    })
    public ResponseEntity<ResponseDto<Void>> handle(RefrigeratorException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.R_00001, e.getMessage()));
    }

    // 재료 관련 Exception
    @ExceptionHandler(value = {
            IngredientException.class
    })
    public ResponseEntity<ResponseDto<Void>> handle(IngredientException e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.badRequest().body(new ResponseDto<>(FailCode.I_00002, e.getMessage()));
    }


    /** ExceptionHandler 정의되지 않은 모든 Exception 발생시 - 서버 Error return */
    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<ResponseDto<Void>> handle(Exception e, WebRequest request) {
        exceptionLogging(e, request);

        return ResponseEntity.internalServerError().body(new ResponseDto<>(FailCode.I_00001, e.getMessage()));
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
