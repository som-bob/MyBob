package com.my.bob.core.service.member;

import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.service.BobUserService;
import com.my.bob.core.domain.member.service.JoinService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
class JoinServiceTest {

    @Autowired
    private BobUserService bobUserService;


    @Autowired
    private JoinService joinService;


    @Test
    @Transactional
    @DisplayName("회원 가입")
    void joinUser(){
        // Given
        String testEmail = "sss@naver.com";
        JoinUserDto joinUser = getJoinUserDto(testEmail);

        // When
        try {
            joinService.joinMember(joinUser);
        } catch (DuplicateUserException e) {
            fail();
        }

        // Then
        assertTrue(bobUserService.existByEmail(testEmail));
    }


    @Test
    @Transactional
    @DisplayName("회원 가입 실패 - 중복 이메일 확인")
    void joinUserFail(){
        // given
        String testEmail = "sss@naver.com";
        JoinUserDto joinUser = getJoinUserDto(testEmail);
        JoinUserDto sameUser = getJoinUserDto(testEmail);
        try {
            joinService.joinMember(joinUser);
        } catch (DuplicateUserException e) {
            fail();
        }

        // when, then
        assertThrows(DuplicateUserException.class, () -> joinService.joinMember(sameUser));
    }

    private static JoinUserDto getJoinUserDto(String testEmail) {
        JoinUserDto joinUserDto = new JoinUserDto();
        joinUserDto.setEmail(testEmail);
        joinUserDto.setPassword("test1234!");
        return joinUserDto;
    }
}