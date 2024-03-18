package com.my.bob.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.my.bob")
public class SecurityConfig {

    private final static String[] PERMIT_ALL = {
            "/user/join",
            "/user/login"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                  // csrf 보안 disable
                .formLogin(AbstractHttpConfigurer::disable)             // formLogin disable
                // 토큰을 통한 로그인. 사용 X session stateless
                .sessionManagement((sessionManager) ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 로그아웃 관련


                // 권한 없이 접근할 api
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(PERMIT_ALL).permitAll().anyRequest().authenticated())

        ;


        return http.build();
    }


    // 암호화와 관련된 모든 설정 값 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
