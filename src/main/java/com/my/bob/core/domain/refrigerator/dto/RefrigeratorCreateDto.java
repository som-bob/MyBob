package com.my.bob.core.domain.refrigerator.dto;

import com.my.bob.core.constants.ErrorMessage;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefrigeratorCreateDto {

    @NotNull(message = ErrorMessage.THIS_IS_REQUIRED)
    private String nickName;
}
