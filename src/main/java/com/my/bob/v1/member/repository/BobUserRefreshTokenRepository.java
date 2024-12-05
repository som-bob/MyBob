package com.my.bob.v1.member.repository;

import com.my.bob.core.domain.member.entity.BobUserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BobUserRefreshTokenRepository extends JpaRepository<BobUserRefreshToken, Long> {

    boolean existsByRefreshToken(String refreshToken);

    Optional<BobUserRefreshToken> findOneByRefreshToken(String refreshToken);

    void deleteByRefreshToken(String refreshToken);

    List<BobUserRefreshToken> findAllByExpiryDateBefore(LocalDateTime dateTime);
}
