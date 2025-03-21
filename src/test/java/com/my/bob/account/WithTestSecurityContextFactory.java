package com.my.bob.account;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithTestSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    @Override
    public SecurityContext createSecurityContext(WithAccount annotation) {
        String email = annotation.value();
        String password = "PASSWORD";
        UserDetails systemUser = User.withUsername(email)
                .password(password) // Noop password encoder for testing
                .roles("USER")
                .build();

        Authentication auth = new TestingAuthenticationToken(systemUser,
                password,
                systemUser.getAuthorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
