package com.my.bob.provider;

import com.my.bob.service.BobUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerUserDetailService implements UserDetailsService {
    // authenticationManager 에서 기본으로 사용하는 provide
    // https://stackoverflow.com/questions/9787409/what-is-the-default-authenticationmanager-in-spring-security-how-does-it-authen

    private final BobUserService bobUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return null;
    }
}
