package com.my.bob.integration.util;

import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.dto.LoginDto;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.exception.NonExistentUserException;
import com.my.bob.core.domain.member.service.JoinService;
import com.my.bob.core.domain.member.service.LoginService;

import static org.assertj.core.api.Assertions.fail;

public class IntegrationTestUtils {

    private static final String TEST_USER_EMAIL = "test__user@test.com";
    private static final String TEST_USER_PASSWORD = "<PASSWORD1234>!";

    public static String getTokenFromTestUser(JoinService joinService, LoginService loginService) {
        // 회원 가입 유저 세팅 + token 발급
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail(TEST_USER_EMAIL);
        dto.setPassword(TEST_USER_PASSWORD);
        dto.setNickName("Test User");
        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            fail("fail to join member.");
        }

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail(TEST_USER_EMAIL);
        loginDto.setPassword(TEST_USER_PASSWORD);
        TokenDto tokenDto = null;
        try {
            tokenDto = loginService.login(loginDto);
        } catch (NonExistentUserException e) {
            fail("fail to login.");
        }
        return tokenDto.getAccessToken();
    }

    public static String getTestUserEmail(){
        return TEST_USER_EMAIL;
    }
}
