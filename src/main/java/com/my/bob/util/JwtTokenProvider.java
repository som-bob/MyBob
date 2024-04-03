package com.my.bob.util;

import com.my.bob.constants.AuthConstant;
import com.my.bob.constants.Authority;
import com.my.bob.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtTokenProvider {

    @Value("${jwt.expire}")
    private int ACCESS_TOKEN_EXPIRE_TIME;   // 30분
    @Value("${jwt.refresh.expire}")
    private int REFRESH_TOKEN_EXPIRE_TIME;  // 7일

    private final Key key;

    private static final String TOKEN_AUTH_KEY = "auth";

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(String email, Authority authority) {
        // TODO check 여러 개의 권한을 받는 것으로 변경?
        Date now = Calendar.getInstance().getTime();
        String accessToken = getToken(email, authority, now, ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = getToken(email, authority, now, REFRESH_TOKEN_EXPIRE_TIME);

        return TokenDto.builder()
                .grantType(AuthConstant.TOKEN_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresMs(ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // token expire time 지났을 경우, ExpiredJwtException 반환 된다
        Claims claims = getClaims(accessToken);

        // 권한 체크
        String authority = claims.get(TOKEN_AUTH_KEY).toString();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<? extends GrantedAuthority> authorities = List.of(simpleGrantedAuthority);

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    /* private method */
    private String getToken(String subject, Authority authority, Date now, int expireTime) {
        Date expireDate = DateUtils.addMilliseconds(now, expireTime);
        Claims claims = Jwts.claims();
        claims.setIssuedAt(now);
        claims.put("auth", authority.name());
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
