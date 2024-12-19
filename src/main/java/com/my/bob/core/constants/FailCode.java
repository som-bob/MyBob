package com.my.bob.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * name: {@code ResponseDto} 객체 내부의 errorCode 세팅 됩니다.
 */
@Getter
@AllArgsConstructor
public enum FailCode {

    I_00001("시스템 내부에 문제가 생겼습니다."),

    V_00001("유효하지 못한 데이터입니다."),

    R_00001("냉장고 기능에 문제가 있습니다."),

    I_00002("재료 기능에 문제가 있습니다."),
    ;

    private final String failMessage;

}
