package com.my.bob.user.repository;

import com.my.bob.user.entity.BobUserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BobUserRefreshTokenRepository extends JpaRepository<BobUserRefreshToken, Integer> {

    boolean existsByRefreshToken(String refreshToken);

    Optional<BobUserRefreshToken> findOneByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
