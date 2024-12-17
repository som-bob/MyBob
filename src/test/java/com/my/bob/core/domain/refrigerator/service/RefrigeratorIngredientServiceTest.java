package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.member.entity.BobUser;
import com.my.bob.core.domain.member.repository.BobUserRepository;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.entity.RefrigeratorIngredient;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorIngredientRepository;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("local")
@DisplayName("나의 냉장고 재료 테스트")
@WithAccount("test_test@test.com")   // 자동으로 해당 계정으로 들어가도록 세팅
class RefrigeratorIngredientServiceTest {

    @Autowired
    private RefrigeratorIngredientService service;

    @Autowired
    private BobUserRepository bobUserRepository;

    @Autowired
    private RefrigeratorRepository refrigeratorRepository;

    @Autowired
    private RefrigeratorIngredientRepository refrigeratorIngredientRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private BobUser mockUser;
    private Refrigerator refrigerator;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        mockUser = bobUserRepository.save(new BobUser("test_test@test.com", "<PASSWORD>", "테스트 유저입니다"));
        ingredient = ingredientRepository.save(new Ingredient("테스트 저장 재료"));
        refrigerator = refrigeratorRepository.save(new Refrigerator("나의 냉장고", mockUser));
    }

    @Test
    @DisplayName("냉장고에 재료 추가 - 성공 케이스")
    void addIngredient_shouldAddNewIngredient() {
        // given
        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredient.getId());
        dto.setAddedDate("2024-12-17");

        // when
        RefrigeratorDto result = service.addIngredient(refrigerator.getId(), dto);
        boolean existsBoolean =
                refrigeratorIngredientRepository.existsByRefrigeratorAndIngredient(refrigerator, ingredient);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getNickName()).isEqualTo("나의 냉장고");
        assertThat(existsBoolean).isTrue();
    }

    @Test
    @DisplayName("냉장고에 재료 중복 추가 방지")
    void addIngredient_shouldNotAddDuplicateIngredient() {
        // given
        // 먼저 저장
        RefrigeratorIngredient refrigeratorIngredient = new RefrigeratorIngredient(refrigerator, ingredient, LocalDate.now());
        refrigeratorIngredientRepository.save(refrigeratorIngredient);

        RefrigeratorAddIngredientDto dto = new RefrigeratorAddIngredientDto();
        dto.setIngredientId(ingredient.getId());
        dto.setAddedDate("2024-12-17");

        // when
        RefrigeratorDto result = service.addIngredient(refrigerator.getId(), dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getIngredients()).hasSize(1); // 중복 저장해도 하나의 재료만 저장 되어 있다
    }
    
    @Test
    @DisplayName("재료 삭제 - 냉장고에 재료가 존재하지 않는 경우 예외 발생")
    void deleteIngredient_shouldThrowException_WhenIngredientNotFound() {
        // TODO 검증
        // given

        // when

        // then
    }

}