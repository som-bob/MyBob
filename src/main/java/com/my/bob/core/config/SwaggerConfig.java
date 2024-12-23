package com.my.bob.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    // http://localhost:8080/swagger-ui/index.html

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MyBob API Document")
                        .version("1.0")
                        .description("API Document for MyBob Project")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                );
    }
}
