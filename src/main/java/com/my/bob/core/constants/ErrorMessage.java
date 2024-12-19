package com.my.bob.core.constants;

public class ErrorMessage {
    // 화면으로 나가는 모든 error message 모음

    private ErrorMessage() {
        throw new IllegalStateException("Utility class");
    }

    // Common
    public static final String THIS_IS_REQUIRED = "필수 값 입니다.";
    public static final String INVALID_REQUEST = "잘못된 요청입니다.";
    public static final String INVALID_DATE = "잘못된 날짜 형식입니다.";

    // 회원
    public static final String INVALID_EMAIL = "올바르지 않은 이메일 형식입니다.";
    public static final String INVALID_PASSWORD = "올바르지 않은 패스워드 형식입니다.";

    public static final String EMPTY_EMAIL = "이메일을 입력해주세요.";
    public static final String EMPTY_PASSWORD = "비밀번호를 입력해주세요.";

    // 유저 관련
    public static final String USER_CANNOT_BE_FOUND = "해당 유저를 찾을 수 없습니다.";
    public static final String NEED_TO_CONFIRM_LOGIN_INFORMATION = "로그인 정보를 확인해주세요.";
    public static final String NEED_TO_CONFIRM_PASSWORD = "비밀번호를 확인해주세요.";


    // 게시판 관련
    public static final String NOT_EXISTENT_POST = "존재하지 않는 게시글입니다.";

    // 냉장고 관련
    public static final String NOT_EXISTENT_REFRIGERATOR = "냉장고가 존재 하지 않습니다.";
    public static final String ALREADY_CREATE_REFRIGERATOR = "이미 냉장고를 생성한 유저입니다.";

    // 재료 관련
    public static final String NOT_EXISTENT_INGREDIENT = "존재하지 않는 재료입니다.";

    // 수정, 삭제 권한 관련
    public static final String DO_NOT_HAVE_PERMISSION = "권한이 없습니다.";

    // 관리자 문의
    public static final String CONTACT_ADMINISTRATOR = "관리자에게 문의하세요.";

}
