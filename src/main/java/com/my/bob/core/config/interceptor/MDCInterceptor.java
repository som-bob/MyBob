package com.my.bob.core.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
public class MDCInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID = "requestId";
    private static final String X_Request_ID = "X-Request-ID";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler)
            throws Exception {
        String requestId = UUID.randomUUID().toString();
        MDC.put(REQUEST_ID, requestId);
        response.addHeader(X_Request_ID, requestId);  // 요청 ID를 응답 헤더에 포함
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler, Exception ex)
            throws Exception {
        // 요청 처리 완류 후 MDC 정리
        MDC.clear();
    }
}
