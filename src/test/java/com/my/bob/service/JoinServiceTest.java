package com.my.bob.service;

import com.my.bob.dto.JoinUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JoinServiceTest {

    @Test
    @Transactional
    @DisplayName("회원 가입")
    public void joinUser(){
//        Given - 테스트를 위한 준비(테스트를 할 수 있는 상황, 객체나 데이터 또는 Mock을 셋팅)
        JoinUserDto joinUserDto = new JoinUserDto();


//        When - 테스트하는 메서드를 실행(어떤 메서드를 실행했을때)


//        Then - 테스트 결과 검증(그 메서드를 실행함으로서 기대 되는 결과)


    }

}