package com.my.bob.v1.member.entity;

import com.my.bob.v1.member.dto.TokenDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "BOB_USER_REFRESH_TOKEN")
public class BobUserRefreshToken {

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tokenId;

    private long userId;

    private String refreshToken;

    private LocalDateTime expiryDate;

    public BobUserRefreshToken(long userId, TokenDto tokenDto) {
        this.userId = userId;
        this.refreshToken = tokenDto.getRefreshToken();
        this.expiryDate = tokenDto.getRefreshTokenExpire();
    }
}
