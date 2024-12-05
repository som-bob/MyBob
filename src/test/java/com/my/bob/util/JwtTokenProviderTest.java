package com.my.bob.util;

import com.my.bob.core.domain.member.constants.Authority;
import com.my.bob.core.domain.member.dto.TokenDto;
import com.my.bob.core.config.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("local")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("Jwt Token 생성 확인")
    void createToken(){
        // given
        String testEmail = "admin@test.com";
        String authority = Authority.ROLE_USER.name() + ", " + Authority.ROLE_ADMIN.name();

        // when
        // 1. 토큰 발급
        // 2. 토큰을 통한 Authentication 휙득
        TokenDto tokenDto = jwtTokenProvider.generateTokenDto(testEmail, authority);
        String accessToken = tokenDto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        // then
        // getName (로그인에 사용된 email 값-)
        String getEmail = authentication.getName();
        assertEquals(testEmail, getEmail);
    }
}