package com.my.bob.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final BobUserRefreshTokenService bobUserRefreshTokenService;

    @Transactional
    public void deleteUsedToken(String refreshToken){
        bobUserRefreshTokenService.deleteByToken(refreshToken);
    }
}
