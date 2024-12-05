package com.my.bob.core.constants;

public class AuthConstant {

    private AuthConstant() {
        throw new IllegalStateException("Constant class");
    }

    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";
}
