package com.my.bob.core.domain.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.bob.core.constants.FailCode;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
public class ResponseDto<T> {

    private T data;

    private String status;

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
     * 실패시 errorCode, errorMessage 세팅
     * @param failCode 실패 코드
     * @param errorMessage 실패 메세지
     */
    public ResponseDto(FailCode failCode, String errorMessage) {
        this.status = "FAIL";
        this.errorCode = failCode.name();
        this.errorMessage = errorMessage;
    }
}
