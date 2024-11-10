package com.my.bob.jwt;

import com.my.bob.constants.AuthConstant;
import com.my.bob.constants.ErrorMessage;
import com.my.bob.exception.BadRequestException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;

public class TokenUtil {

    public static String parsingToken(String authHeader){
        if(StringUtils.isEmpty(authHeader) || ! authHeader.startsWith(AuthConstant.TOKEN_TYPE)) return Strings.EMPTY;
        // String form is 'Bearer jwtTokenValue'
        String[] tokenSplit = authHeader.split(" ");
        return tokenSplit[1];
    }

    public static String getTokenFromHeader(HttpServletRequest request)
            throws BadRequestException {
        String requestHeader = request.getHeader(AuthConstant.AUTH_HEADER);
        if(org.apache.commons.lang3.StringUtils.isEmpty(requestHeader)) {
            throw new BadRequestException(ErrorMessage.INVALID_REQUEST);
        }

        // check refreshToken
        return parsingToken(requestHeader);
    }
}
