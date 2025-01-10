package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.member.dto.request.JoinUserDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.service.JoinService;
import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.exception.RefrigeratorAlreadyExist;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@WithAccount("system@system.com")
@Transactional
@DisplayName("냉장고 테스트")
class RefrigeratorServiceTest {
    // 냉장고 재료 추가, 삭제 테스트가 없음!

    @Autowired
    RefrigeratorService refrigeratorService;

    @Autowired
    JoinService joinService;

    private final String testUserEmail = "is_test_user@test.com";

    @BeforeEach
    void initUser() throws DuplicateUserException {
        JoinUserDto joinUserDto = new JoinUserDto();
        joinUserDto.setEmail(testUserEmail);
        joinUserDto.setPassword("<PASSWORD>!");
        joinUserDto.setNickName("testUser");

        joinService.joinMember(joinUserDto);
    }

    @Test
    @DisplayName("신규 냉장고 생성")
    void createRefrigerator() {
        // given
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("나의 냉장고");

        // when
        RefrigeratorDto refrigeratorDto = refrigeratorService.createRefrigerator(testUserEmail, dto);

        // then
        assertThat(refrigeratorDto.getRefrigeratorId()).isPositive();
        assertThat(refrigeratorDto.getNickName()).isEqualTo("나의 냉장고");
        assertThat(refrigeratorDto.getIngredients()).size().isEqualTo(0);
    }

    @Test
    @DisplayName("존재 하지 않는 유저 신규 냉장고 생성 에러 발생")
    void failToCreateRefrigerator() {
        // given
        String failTestEmail = "<EMAIL>";
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("나의 냉장고");

        // when, then
        assertThatThrownBy(() -> refrigeratorService.createRefrigerator(failTestEmail, dto))
                .isInstanceOf(UsernameNotFoundException.class);

    }

    @Test
    @DisplayName("냉장고를 두 번 생성할 경우 에러 발생")
    void failToDuplicateRefrigerator() {
        // given
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("나의 냉장고");

        // when
        refrigeratorService.createRefrigerator(testUserEmail, dto);

        // then
        assertThatThrownBy(() -> refrigeratorService.createRefrigerator(testUserEmail, dto))
                .isInstanceOf(RefrigeratorAlreadyExist.class);
    }

    @Test
    @DisplayName("냉장고 조회 하기")
    void getRefrigerator() {
        // given
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("나의 냉장고");
        RefrigeratorDto refrigeratorDto = refrigeratorService.createRefrigerator(testUserEmail, dto);

        // when
        RefrigeratorDto getRefrigerator = refrigeratorService.getRefrigerator(testUserEmail);

        // then
        assertThat(refrigeratorDto).isEqualTo(getRefrigerator);
    }


}