package com.my.bob.v1.member.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenDto {

    private String grantType;                   // JWT 토큰의 인증 방법

    private String accessToken;                 // accessToken 값

    private LocalDateTime accessTokenExpire;    // accessToken 인증 만료 시간

    private String refreshToken;                // refreshToken 값

    private LocalDateTime refreshTokenExpire;   // refreshToken 인증 만료 시간
}
