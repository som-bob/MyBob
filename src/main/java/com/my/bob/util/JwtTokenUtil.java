package com.my.bob.util;

import com.my.bob.constants.AuthConstant;
import com.my.bob.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.expire}")
    private int ACCESS_TOKEN_EXPIRE_TIME;   // 30분
    @Value("${jwt.refresh.expire}")
    private int REFRESH_TOKEN_EXPIRE_TIME;  // 7일

    private final Key key;

    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(String email) {
        Date now = Calendar.getInstance().getTime();
        String accessToken = getToken(email, now, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = getToken(email, now, REFRESH_TOKEN_EXPIRE_TIME);

        return TokenDto.builder()
                .grantType(AuthConstant.AUTH_HEADER)
                .accessToken(accessToken)
                .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .build();
    }

    private String getToken(String subject, Date now, int expireTime) {
        Date expireDate = DateUtils.addSeconds(now, expireTime);
        Claims claims = Jwts.claims();
        claims.setIssuedAt(now);
        claims.setExpiration(expireDate);
        claims.setSubject(subject);

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


}
