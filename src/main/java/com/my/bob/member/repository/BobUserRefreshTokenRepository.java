package com.my.bob.member.repository;

import com.my.bob.member.entity.BobUserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BobUserRefreshTokenRepository extends JpaRepository<BobUserRefreshToken, Integer> {

    boolean existsByRefreshToken(String refreshToken);

    boolean existsByUserId(int userId);

    void deleteByUserId(int userId);

    Optional<BobUserRefreshToken> findOneByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);
}
