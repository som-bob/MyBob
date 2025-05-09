package com.my.bob.core.config;

import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InMemoryHttpExchangeRepositoryConfig {

    @Bean
    public InMemoryHttpExchangeRepository inMemoryHttpExchangeRepository(){
        return new InMemoryHttpExchangeRepository();
    }
}
