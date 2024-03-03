package com.my.bob.config.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDto {

    private String grantType;

    private String accessToken;

    private long accessTokenExpiresIn;

    private String refreshToken;
}
