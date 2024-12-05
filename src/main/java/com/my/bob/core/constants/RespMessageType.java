package com.my.bob.core.constants;

import com.my.bob.core.constants.interfaces.EnumPropertyType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RespMessageType implements EnumPropertyType {

    SYSTEM_ERROR("시스템 에러가 발생했습니다. 관리자에게 문의하세요."),
    INVALID_ACCESS("유효하지 않은 접근입니다."),
    FILE_DOWNLOAD_ERROR("파일 다운로드 중 시스템 문제가 발생했습니다. 관리자에게 문의하세요."),
    ;

    private final String title;


    @Override
    public String getCode() {
        return name();
    }

    @Override
    public String getTitle() {
        return title;
    }
}
