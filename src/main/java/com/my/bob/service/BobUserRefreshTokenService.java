package com.my.bob.service;

import com.my.bob.dto.TokenDto;
import com.my.bob.entity.BobUserRefreshToken;
import com.my.bob.repository.BobUserRefreshTokenRepository;
import com.my.bob.util.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BobUserRefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final BobUserRefreshTokenRepository bobUserRefreshTokenRepository;

    public boolean isExists(String refreshToken) {
        return bobUserRefreshTokenRepository.existsByRefreshToken(refreshToken);
    }

    public BobUserRefreshToken getByToken(String refreshToken) {
        Optional<BobUserRefreshToken> optionToken = bobUserRefreshTokenRepository.findOneByRefreshToken(refreshToken);
        return optionToken.orElseThrow(() -> new BadCredentialsException("토큰 정보가 잘못됐습니다."));
    }

    // TODO refresh Token 저장하는 부분 만들기
    public void saveTokenByTokenDto(TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        Claims claims = jwtTokenProvider.getClaims(refreshToken);


    }
}
