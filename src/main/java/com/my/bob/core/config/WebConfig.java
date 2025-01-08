package com.my.bob.core.config;

import com.my.bob.core.config.interceptor.CustomerInterceptor;
import com.my.bob.core.config.interceptor.MDCInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CustomerInterceptor customerInterceptor;

    private final MDCInterceptor mdcInterceptor;

    public WebConfig(CustomerInterceptor customerInterceptor,
                     MDCInterceptor mdcInterceptor) {
        this.customerInterceptor = customerInterceptor;
        this.mdcInterceptor = mdcInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 인터셉터를 등록하는 메서드
        // CustomInterceptor를 등록하고, 모든 URL에 대해 인터셉터를 적용하도록 설정
        registry.addInterceptor(mdcInterceptor);
        registry.addInterceptor(customerInterceptor).addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
