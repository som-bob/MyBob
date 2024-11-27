package com.my.bob.v1.common.service;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareService implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        // User 객체의 userName 값이 SpringContextHolder 안에 담겨있는 유저 이메일 정보
        Object principal = authentication.getPrincipal();
        if(principal instanceof User user) {
            return Optional.of((user).getUsername());
        }

        return Optional.empty();
    }
}
