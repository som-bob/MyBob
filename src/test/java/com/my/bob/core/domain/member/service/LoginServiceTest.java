package com.my.bob.core.domain.member.service;

import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.dto.LoginDto;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.exception.NonExistentUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("로그인")
class LoginServiceTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private JoinService joinService;

    private final String email = "test__user@test.com";
    private final String password = "correctPassword1234!";

    @Test
    @DisplayName("로그인 - 성공")
    void login(){
        // given
        joinTestUser();

        LoginDto dto = new LoginDto();
        dto.setEmail(email);
        dto.setPassword(password);

        // when
        TokenDto tokenDto = null;
        try {
            tokenDto = loginService.login(dto);
        } catch (NonExistentUserException e) {
            fail("Fail to login.");
        }

        // then
        assertThat(tokenDto).isNotNull();
        assertThat(tokenDto.getAccessToken()).isNotNull();
        assertThat(tokenDto.getAccessTokenExpire()).isAfter(LocalDateTime.now());
        assertThat(tokenDto.getRefreshToken()).isNotNull();
        assertThat(tokenDto.getRefreshTokenExpire()).isAfter(LocalDateTime.now());

    }

    private void joinTestUser() {
        JoinUserDto dto = new JoinUserDto();
        dto.setEmail(email);
        dto.setPassword(password);

        try {
            joinService.joinMember(dto);
        } catch (DuplicateUserException e) {
            fail("Fail to join member.");
        }
    }

}