package com.my.bob.core.service.refrigerator;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.member.dto.JoinUserDto;
import com.my.bob.core.domain.member.exception.DuplicateUserException;
import com.my.bob.core.domain.member.service.JoinService;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorCreateDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.v1.refrigerator.service.RefrigeratorServiceImpl;
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
@ActiveProfiles("local")
@WithAccount("system@system.com")
@Transactional
class RefrigeratorServiceImplTest {

    @Autowired
    RefrigeratorServiceImpl refrigeratorServiceImpl;

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
        RefrigeratorDto refrigeratorDto = refrigeratorServiceImpl.createRefrigerator(testUserEmail, dto);

        // then
        assertThat(refrigeratorDto.getRefrigeratorId()).isPositive();
        assertThat(refrigeratorDto.getNickName()).isEqualTo("나의 냉장고");
        assertThat(refrigeratorDto.getIngredients()).size().isEqualTo(0);
    }

    @Test
    @DisplayName("존재 하지 않는 유저 신규 냉장고 생성")
    void failToCreateRefrigerator() {
        // given
        String failTestEmail = "<EMAIL>";
        RefrigeratorCreateDto dto = new RefrigeratorCreateDto();
        dto.setNickName("나의 냉장고");

        // when, then
        assertThatThrownBy(() -> refrigeratorServiceImpl.createRefrigerator(failTestEmail, dto))
                .isInstanceOf(UsernameNotFoundException.class);

    }


}