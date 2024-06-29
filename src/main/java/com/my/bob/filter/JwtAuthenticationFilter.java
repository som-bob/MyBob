package com.my.bob.filter;

import com.my.bob.constants.AuthConstant;
import com.my.bob.jwt.JwtTokenProvider;
import com.my.bob.jwt.TokenUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter
    // 동일한 request 안에서 한번만 실행되는 필터로, 현재 프로젝트에선 jwt token 인증에 사용된다
    // UsernamePasswordAuthenticationFilter 전에  authentication 객체를 셋팅해 둔다
    // 해당 filter의 경우 api/a가 끝난후 api/b로 리다이렉트 될 경우 Filter가 두번 호출되거나 하는 것을 막을 수 있다

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AuthConstant.AUTH_HEADER);
        String token = TokenUtil.parsingToken(authHeader);
        if(! StringUtils.isEmpty(token)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
