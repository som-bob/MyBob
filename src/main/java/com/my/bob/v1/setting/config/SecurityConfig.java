package com.my.bob.v1.setting.config;

import com.my.bob.v1.setting.filter.JwtAuthenticationFilter;
import com.my.bob.v1.setting.handler.CustomerAccessDeniedHandler;
import com.my.bob.v1.setting.jwt.JwtTokenProvider;
import com.my.bob.v1.setting.point.CustomerAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration          // Spring의 설정 클래스임을 나타내는 어노테이션
@EnableWebSecurity      // Spring Security를 활성화하는 어노테이션
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.my.bob")
public class SecurityConfig {

    @Value("${client.host.url}")
    private String clientHost;


    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] PERMIT_ALL = {
            "/test/**",
            "/member/join",
            "/member/login",
            "/member/reissue"

            ,"/manage/actuator/**" // actuator 기능 우선 권한 없이 실행
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                  // csrf 보안 disable
                .formLogin(AbstractHttpConfigurer::disable)             // formLogin disable
                .httpBasic(AbstractHttpConfigurer::disable)             // BasicHttp disable

                // cors 설정
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))

                // 토큰을 통한 로그인. 사용 X session stateless
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 로그아웃 관련

                // 권한 없이 접근할 api
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(PERMIT_ALL).permitAll().anyRequest().authenticated())

                // 로그인 관련
                // UsernamePasswordAuthenticationFilter 전에 jwt Token 관련 Filter 진행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                // 인증, 인가와 관련된 예어 처리 추가
                .exceptionHandling(exceptionHandling  -> exceptionHandling
                        .accessDeniedHandler(new CustomerAccessDeniedHandler())  // AccessDeniedHandler 설정
                        .authenticationEntryPoint(new CustomerAuthenticationEntryPoint())  // AuthenticationEntryPoint 설정
                )
        ;
        return http.build();
    }


    // 암호화와 관련된 모든 설정 값 등록
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Spring 서버 전역적으로 CORS 설정
    private CorsConfigurationSource corsConfigurationSource(){
        return request -> {
            CorsConfiguration config = new CorsConfiguration();

            // 리소스 허용 URL
            List<String> urlList = new ArrayList<>();
            urlList.add(clientHost);
            config.setAllowedOrigins(urlList);

            // 허용하는 Http Method
            List<String> methods = new ArrayList<>();
            methods.add("GET");
            methods.add("POST");
            methods.add("PUT");
            methods.add("DELETE");
            config.setAllowedMethods(methods);

            // 모든 header 허용
            config.setAllowedHeaders(Collections.singletonList("*"));

            //인증, 인가를 위한 credentials 를 TRUE로 설정
            config.setAllowCredentials(true);

            return config;
        };
    }

}
