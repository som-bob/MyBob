package com.my.bob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BobApplication {
	// TODO flyway 버전링 추천받기
	// TODO 스프링 배치를 통해 refresh_token 삭제하도록 변경
	// TODO repository 테스트 코드 추천 / test 환경계 만들지 고려해서

	public static void main(String[] args) {
		SpringApplication.run(BobApplication.class, args);
	}

}
