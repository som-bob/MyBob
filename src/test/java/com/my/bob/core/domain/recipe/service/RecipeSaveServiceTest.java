package com.my.bob.core.domain.recipe.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("레시피 저장, 업데이트 테스트")
@WithAccount("system@system.com")   // 자동으로 해당 계정으로 들어가도록 세팅
class RecipeSaveServiceTest {

    @Autowired
    RecipeSaveService recipeSaveService;

    @Test
    @DisplayName("레시피 저장 테스트")
    void saveRecipe() {
        // given
        RecipeCreateDto dto = new RecipeCreateDto();
        dto.setRecipeName("레시피");
        dto.setDifficulty(Difficulty.ANYONE);

        // when
        long savedId = recipeSaveService.newRecipe(dto);

        // then
        assertThat(savedId).isPositive();
    }

}