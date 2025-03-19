package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.dto.response.IngredientDto;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveIngredient;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("재료 테스트")
class IngredientServiceTest {

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setUp() {
        // 기본 재료 저장
        saveIngredient(ingredientRepository, "나_테스트 재료");
        saveIngredient(ingredientRepository, "다_테스트 재료");
        saveIngredient(ingredientRepository, "가_테스트 재료");
    }

    @AfterEach
    void tearDown() {
        ingredientRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("모든 재료 조회")
    void getAllIngredients() {
        // given & when
        List<IngredientDto> allIngredients = ingredientService.getAllIngredients();

        // then
        assertThat(allIngredients)
                .isNotNull()
                .hasSize(3)
                .extracting(IngredientDto::getIngredientName)
                .containsExactly("가_테스트 재료", "나_테스트 재료", "다_테스트 재료");
    }
}
