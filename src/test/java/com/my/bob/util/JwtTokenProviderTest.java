package com.my.bob.util;

import com.my.bob.constants.Authority;
import com.my.bob.dto.TokenDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Jwt Token 생성 확인")
    public void createToken(){
        // given
        String testEmail = "test@test.com";
        Authority authority = Authority.ROLE_USER;

        // when
        // 1. 토큰 발급
        // 2. 토큰을 통한 Authentication 휙득
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(testEmail, authority);
        String accessToken = tokenDto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // then
        // getName (로그인에 사용된 email 값)
        String getEmail = authentication.getName();
        assertEquals(testEmail, getEmail);
    }

    @Test
    @DisplayName("Jwt Token 유효성 검증")
    public void validateToken(){

    }



}