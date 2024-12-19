package com.my.bob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BobApplication {
	// TODO 스프링 배치를 통해 refresh_token 삭제하도록 변경

	public static void main(String[] args) {
		SpringApplication.run(BobApplication.class, args);
	}

}
