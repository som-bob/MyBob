package com.my.bob.jwt;

import com.my.bob.constants.AuthConstant;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.util.Strings;

public class TokenUtil {

    public static String parsingToken(String authHeader){
        if(StringUtils.isEmpty(authHeader) || ! authHeader.startsWith(AuthConstant.TOKEN_TYPE)) return Strings.EMPTY;
        // String form is 'Bearer jwtTokenValue'
        String[] tokenSplit = authHeader.split(" ");
        return tokenSplit[1];
    }
}
