package com.my.bob.core.domain.recipe.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.exception.RecipeDeletedException;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("레시피 삭제 테스트")
@WithAccount("system@system.com")   // 자동으로 해당 계정으로 들어가도록 세팅
class RecipeDeleteServiceTest {

    @Autowired
    private RecipeDeleteService recipeDeleteService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeServiceHelper recipeServiceHelper;

    private Integer recipeId;

    @BeforeEach
    void setUp() {
        // 레시피 저장
        Recipe recipe = new Recipe("1번 레시피", "1번 테스트 레시피", Difficulty.ANYONE, "인분", (short) 30);
        recipeRepository.save(recipe);

        recipeId = recipe.getId();
    }

    @Test
    @DisplayName("레시피 삭제, 삭제 후 조회 테스트")
    void recipeDelete() {
        // given
        recipeDeleteService.delete(recipeId);

        // when & then
        assertThatThrownBy(() -> recipeServiceHelper.getRecipe(recipeId))
                .isInstanceOf(RecipeDeletedException.class);
    }

}