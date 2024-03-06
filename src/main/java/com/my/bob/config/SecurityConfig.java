package com.my.bob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    // 암호화와 관련된 모든 설정 값 등록

    @Bean
    public PasswordEncoder passwordEncoder(){


        return new BCryptPasswordEncoder();
    }


}
