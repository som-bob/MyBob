package com.my.bob.core.config;

import com.my.bob.core.constants.AuthConstant;
import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.member.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.expire}")
    private int ACCESS_TOKEN_EXPIRE_TIME;   // 30분
    @Value("${jwt.refresh.expire}")
    private int REFRESH_TOKEN_EXPIRE_TIME;  // 7일

    private final Key key;

    private static final String TOKEN_AUTH_KEY = "auth";

    // 컴포넌트 스캔을 통한 싱글톤 등록시 secretKey 값을 세팅
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(String email, String authority) {
        Date now = Calendar.getInstance().getTime();

        Date accessTokenExpireDate = DateUtils.addMilliseconds(now, ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenExpireDate = DateUtils.addMilliseconds(now, REFRESH_TOKEN_EXPIRE_TIME);
        String accessToken = getToken(email, authority, accessTokenExpireDate);
        String refreshToken = getToken(email, authority, refreshTokenExpireDate);

        LocalDateTime accessTokenExpire = accessTokenExpireDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime refreshTokenExpire = refreshTokenExpireDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();


        return TokenDto.builder()
                .grantType(AuthConstant.TOKEN_TYPE)
                .accessToken(accessToken)
                .accessTokenExpire(accessTokenExpire)
                .refreshToken(refreshToken)
                .refreshTokenExpire(refreshTokenExpire)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = getClaims(accessToken);

        // 권한 체크
        String authority = claims.get(TOKEN_AUTH_KEY).toString();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
        Collection<? extends GrantedAuthority> authorities = List.of(simpleGrantedAuthority);

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Claims getClaims(String token) {
        try{
            // token expire time 지났을 경우, ExpiredJwtException 반환
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            log.error("getClaims Error: ", e);
            throw new AccessDeniedException(ErrorMessage.INVALID_REQUEST);  // ExpiredJwtException 포함
        }
    }


    private String getToken(String subject, String authority, Date expireDate) {
        Claims claims = Jwts.claims();
        claims.setIssuedAt(expireDate);
        claims.put("auth", authority);
        claims.setExpiration(expireDate);
        claims.setSubject(subject);

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(expireDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
