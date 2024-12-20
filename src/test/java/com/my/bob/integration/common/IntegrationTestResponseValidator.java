package com.my.bob.integration.common;

import com.my.bob.core.constants.FailCode;
import com.my.bob.core.domain.base.dto.ResponseDto;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class IntegrationTestResponseValidator {

    public static void assertSuccessResponse(ResponseDto<?> responseDto) {
        assertThat(responseDto.getStatus()).isEqualTo("SUCCESS");
    }

    public static void assertFailResponse(ResponseDto<?> responseDto,
                                          FailCode failCode,
                                          String errorMessage) {
        assertThat(responseDto.getErrorCode()).isEqualTo(failCode.name());
        assertThat(responseDto.getStatus()).isEqualTo("FAIL");
        assertThat(responseDto.getErrorMessage()).isEqualTo(errorMessage);
    }
}
