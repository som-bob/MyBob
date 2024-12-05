package com.my.bob.core.constants;

public class ErrorMessage {

    private ErrorMessage() {
        throw new IllegalStateException("Utility class");
    }

    // 회원
    public static final String INVALID_EMAIL = "올바르지 않은 이메일 형식입니다.";
    public static final String INVALID_PASSWORD = "올바르지 않은 패스워드 형식입니다.";

    public static final String EMPTY_EMAIL = "이메일을 입력해주세요.";
    public static final String EMPTY_PASSWORD = "비밀번호를 입력해주세요.";

    // Common
    public static final String INVALID_REQUEST = "잘못된 요청입니다.";

    // 유저 관련
    public static final String USER_CANNOT_BE_FOUND = "해당 유저를 찾을 수 없습니다.";

    // 게시판 관련
    public static final String NON_EXISTENT_POST = "존재하지 않는 게시글입니다.";

    // 수정, 삭제 권한 관련
    public static final String DO_NOT_HAVE_PERMISSION = "권한이 없습니다.";


    // 관리자 문의
    public static final String CONTACT_ADMINISTRATOR = "관리자에게 문의하세요.";


}
