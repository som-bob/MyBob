package com.my.bob.provider;

import com.my.bob.dto.TokenDto;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    @Value("${jwt.expire}")
    private static long ACCESS_TOKEN_EXPIRE_TIME;   // 30분
    @Value("${jwt.refresh.expire}")
    private static long REFRESH_TOKEN_EXPIRE_TIME;  // 7일

    private final Key key;


    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {

        return null;
    }
}
