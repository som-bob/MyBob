package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.external.redis.service.RecentRecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.fail;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("최근 레시피 테스트")
class RecentRecipeServiceTest {

    @Autowired
    private RecentRecipeService recentRecipeService;

    @Test
    @DisplayName("레시피 저장 테스트")
    void saveRecentRecipe() {
        // given


        // when
        
        // then
        fail("fail");
    }

}
