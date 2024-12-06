package com.my.bob.core.service.member;

import com.my.bob.core.domain.member.entity.BobUserRefreshToken;

import java.util.List;

public interface BobUserRefreshTokenService {

    boolean isExists(String refreshToken);

    BobUserRefreshToken getByToken(String refreshToken);

    void saveRefreshToken(BobUserRefreshToken userRefreshToken);

    void deleteByToken(String refreshToken);

    List<BobUserRefreshToken> getAllAlreadyExpired();

    void deleteAll(List<BobUserRefreshToken> refreshTokens);
}
