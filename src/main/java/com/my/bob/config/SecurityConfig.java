package com.my.bob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.my.bob")
public class SecurityConfig {

    private final static String[] PERMIT_ALL = {
            "/",
            "/user/join"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // csrf 보안 사용 X
                .csrf(AbstractHttpConfigurer::disable)


                .authorizeHttpRequests(requests -> requests.requestMatchers(PERMIT_ALL).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }


    // 암호화와 관련된 모든 설정 값 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
