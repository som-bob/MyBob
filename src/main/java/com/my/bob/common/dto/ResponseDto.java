package com.my.bob.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private T data;
    private String status;  // 성공시에는 SUCCESS, 실패시에는 errorCode
    private String errorCode;
    private String errorMessage;

    /**
     * 성공시 빈 객체로 return
     */
    public ResponseDto(){
        this.status = "SUCCESS";
    }

    /**
     * 성공시 data 세팅
     * @param data 성공 데이터
     */
    public ResponseDto(T data) {
        this.data = data;
        this.status = "SUCCESS";
    }

    /**
     * FailCode 정의된 error code, message return
     * @param failCode 정의된 fail code, message 값
     */
    public ResponseDto(FailCode failCode) {
        this.status = "FAIL";
        this.errorCode = failCode.name();
        this.errorMessage = failCode.failMessage;
    }

    /**
     * 실패시 errorCode, errorMessage 세팅
     * @param failCode 실패 코드
     * @param errorMessage 실패 메세지
     */
    public ResponseDto(FailCode failCode, String errorMessage) {
        this.status = "FAIL";
        this.errorCode = failCode.name();
        this.errorMessage = errorMessage;
    }

    // 실패 코드 정의
    @Getter
    @AllArgsConstructor
    public enum FailCode {
        I_00001("시스템 내부에 문제가 생겼습니다."),

        V_00001("유효하지 못한 데이터입니다."),
        ;

        private final String failMessage;

    }
}
