package com.my.bob.service;

import com.my.bob.dto.TokenDto;
import com.my.bob.entity.BobUserRefreshToken;
import com.my.bob.repository.BobUserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BobUserRefreshTokenService {

    private final BobUserRefreshTokenRepository bobUserRefreshTokenRepository;

    public boolean isExists(String refreshToken) {
        return bobUserRefreshTokenRepository.existsByRefreshToken(refreshToken);
    }

    public BobUserRefreshToken getByToken(String refreshToken) {
        Optional<BobUserRefreshToken> optionToken = bobUserRefreshTokenRepository.findOneByRefreshToken(refreshToken);
        return optionToken.orElseThrow(() -> new BadCredentialsException("토큰 정보가 잘못됐습니다."));
    }

    @Transactional
    public void saveTokenByTokenDto(int userId, TokenDto tokenDto) {
        String refreshToken = tokenDto.getRefreshToken();
        LocalDateTime refreshTokenExpire = tokenDto.getRefreshTokenExpire();

        BobUserRefreshToken userRefreshToken = BobUserRefreshToken.builder()
                .refreshToken(refreshToken)
                .userId(userId)
                .expiryDate(refreshTokenExpire).build();
        bobUserRefreshTokenRepository.save(userRefreshToken);
    }

    // TODO 로그아웃 할 때 그 계정의 리프레쉬 토큰을 지워주는 것을 추가
}
