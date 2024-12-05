package com.my.bob.core.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class CustomerInterceptor implements HandlerInterceptor {
    // DispatcherServlet --> Interceptor --> Controller


    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 요청이 Controller에 도달하기 전에 실행되는 메서드
        // true를 반환하면 요청이 계속 진행되고, false를 반환하면 요청을 중단합니다.
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        // Controller가 실행된 후, View가 렌더링되기 전에 실행되는 메서드
        // Controller가 정상적으로 실행된 이후에 실행되는 메소드이다. Controller에서 예외가 발생한다면, postHandle() 메소드는 실행되지 않는다.
        // 최근에는 Json 형태로 데이터를 제공하는 RestAPI 기반의 컨트롤러(@RestController)를 만들면서 자주 사용되지는 않는다.
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        // View가 렌더링된 후에 실행되는 메서드
        // View가 클라이언트에 응답을 전송한 뒤에 실행된다.
        // 컨트롤러 실행과정에서 예외가 발생한 경우 해당 예외가 afterCompletion() 메소드의 4번째 파라미터로 전달되어, 로그로 남기는 등 후처리를 위해 사용될 수 있다.
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
