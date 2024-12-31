package com.my.bob.core.domain.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.bob.core.constants.FailCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // null 값 필드는 JSON 응답에서 제외
@Schema(description = "공통 응답 DTO")
public class ResponseDto<T> {

    @Schema(description = "응답 데이터", nullable = true)
    private T data;

    @Schema(description = "응답 상태(성공시에는 SUCCESS, 실패시에는 errorCode)", example = "SUCCESS")
    private String status;

    @Schema(description = "오류 코드(실패 시 표시)", example = "I_00001", nullable = true)
    // errorCode와 errorMessage에 대해 ConditionalSerializer를 사용하여 status 값이 FAIL일 때만 직렬화
    @JsonSerialize(using = ConditionalSerializer.class)
    private String errorCode;

    @Schema(description = "오류 메세지(실패 시 표시)", example = "exception 메세지", nullable = true)
    @JsonSerialize(using = ConditionalSerializer.class)
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

    /**
     * status가 FAIL인지 여부 반환 (직렬화 조건에서 사용)
     */
    @JsonIgnore
    public boolean isFail() {
        return this.status.equals("FAIL");
    }
}
