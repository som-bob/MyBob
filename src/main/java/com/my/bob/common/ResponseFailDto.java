package com.my.bob.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class ResponseFailDto {

    private String errorCode;
    private String errorMessage;

    @Getter
    @AllArgsConstructor
    public enum ErrorCode {
        IN_00001("ERROR_USED_INTERNALLY."),
        ;

        private final String message;
    }
}
