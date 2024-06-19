package com.my.bob.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.bob.user.dto.CommonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomerAccessDeniedHandler extends AccessDeniedHandlerImpl {
    // TODO handler 붙이기

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        // 우선 forbidden 준다
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setError(HttpStatus.UNAUTHORIZED, "access Denied");

        PrintWriter responseWriter = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        responseWriter.write(objectMapper.writeValueAsString(commonResponse));
        responseWriter.flush();
        responseWriter.close();
    }
}
