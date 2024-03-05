package com.my.bob.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.my.bob.constants.RespMessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

    private String result;
    private int status = HttpStatus.OK.value();
    private Integer errorCode;
    private String message;
    private Object paging;
    private Object data;

    public CommonResponse(Object data) {
        setData(data);
    }

    public void setError(HttpStatus httpStatus, String message) {
        setResult("ERROR");
        setStatus(httpStatus.value());
        setMessage(message);
    }

    public void setError(HttpStatus httpStatus, RespMessageType respMessageType) {
        setResult("ERROR");
        setStatus(httpStatus.value());
        setMessage(respMessageType.getTitle());
    }
}
