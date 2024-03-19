package com.my.bob.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.bob.entity.BobUser;
import com.my.bob.exception.InputNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class CustomerAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // DispatcherServlet으로 가기 전에 인증과 관련된 Filter 추가


    public CustomerAuthenticationFilter(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    // TODO 분석
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        final UsernamePasswordAuthenticationToken authRequest;

        try {
            final BobUser bobUser = new ObjectMapper().readValue(request.getInputStream(), BobUser.class);
            authRequest = new UsernamePasswordAuthenticationToken(bobUser.getEmail(), bobUser.getPassword());
        } catch (IOException e) {
            throw new InputNotFoundException();
        }

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
