package com.my.bob.v1.setting.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        // TODO 차후 LocalDateTime 값 -> String 변경 로직 넣기

        return new ModelMapper();
    }
}
