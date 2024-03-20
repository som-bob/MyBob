package com.my.bob.util;

import com.my.bob.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.expire}")
    private static int ACCESS_TOKEN_EXPIRE_TIME;   // 30분
    @Value("${jwt.refresh.expire}")
    private static int REFRESH_TOKEN_EXPIRE_TIME;  // 7일

    private final Key key;

    public JwtTokenUtil(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Authentication authentication) {
        Date now = Calendar.getInstance().getTime();
        Date expireDate = DateUtils.addSeconds(now, ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshDate = DateUtils.addSeconds(now, REFRESH_TOKEN_EXPIRE_TIME);

        Claims claims = Jwts.claims();
        claims.setIssuedAt(now);
        claims.setExpiration(expireDate);
        claims.setSubject(authentication.getName());
        claims.put("reissuance", refreshDate);

        String token = Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return null;
    }


}
