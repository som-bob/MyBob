package com.my.bob.member.service;

import com.my.bob.member.entity.BobUserRefreshToken;
import com.my.bob.member.repository.BobUserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    public void saveRefreshToken(BobUserRefreshToken userRefreshToken){
        bobUserRefreshTokenRepository.save(userRefreshToken);
    }

    @Transactional
    public void deleteByToken(String refreshToken) {
        bobUserRefreshTokenRepository.deleteByRefreshToken(refreshToken);
    }

    public List<BobUserRefreshToken> getAllAlreadyExpired(){
        return bobUserRefreshTokenRepository.findAllByExpiryDateBefore(LocalDateTime.now());
    }

    @Transactional
    public void deleteAll(List<BobUserRefreshToken> refreshTokens) {
        bobUserRefreshTokenRepository.deleteAll(refreshTokens);
    }
}
