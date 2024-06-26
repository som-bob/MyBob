package com.my.bob.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CustomerAccessDeniedHandler extends AccessDeniedHandlerImpl {
    // HttpStatus 403 Forbidden은 서버가 해당 요청을 이해하였으나, 사용자의 권한이 부족하여 요청이 거부된 상태를 말한다.

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        // CommonResponse 말고 UNAUTHORIZED 준다
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);      // JSON 타입 반환
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());   // 한국어 허용

//        CommonResponse commonResponse = new CommonResponse(HttpStatus.UNAUTHORIZED);
//        PrintWriter responseWriter = response.getWriter();
//        ObjectMapper objectMapper = new ObjectMapper();
//        responseWriter.write(objectMapper.writeValueAsString(commonResponse));
//        responseWriter.flush();
//        responseWriter.close();
    }
}
