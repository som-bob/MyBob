package com.my.bob.core.domain.recipe.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;
import com.my.bob.core.domain.recipe.dto.request.RecipeDetailCreateDto;
import com.my.bob.core.domain.recipe.dto.request.RecipeIngredientCreateDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeDetail;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.my.bob.util.ResourceUtil.getFileFromResource;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("레시피 저장, 업데이트 테스트")
@WithAccount("system@system.com")   // 자동으로 해당 계정으로 들어가도록 세팅
class RecipeSaveServiceTest {

    @Autowired
    RecipeSaveService recipeSaveService;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private List<Ingredient> ingredientList;

    @BeforeEach
    void setUp() {
        // 기본 재료 저장
        Ingredient ingredient1 = ingredientRepository.save(new Ingredient("가_테스트 재료"));
        Ingredient ingredient2 = ingredientRepository.save(new Ingredient("나_테스트 재료"));
        Ingredient ingredient3 = ingredientRepository.save(new Ingredient("다_테스트 재료"));

        ingredientList = List.of(ingredient1, ingredient2, ingredient3);
    }

    @Test
    @DisplayName("레시피 저장 테스트")
    @Transactional
    void saveRecipe() throws IOException {
        // given
        RecipeCreateDto dto = createRecipeData();
        MultipartFile recipeFile = getFileFromResource("test.png");
        Map<String, MultipartFile> recipeDetailsFiles = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            MultipartFile recipeDetail = getFileFromResource(String.format("test%d.png", i));
            recipeDetailsFiles.put(String.valueOf(i - 1), recipeDetail);
        }

        // when
        int recipeId = recipeSaveService.newRecipe(dto, recipeFile, recipeDetailsFiles);
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        /* then */
        // 레시피
        assertThat(recipeOptional).isPresent();
        Recipe recipe = recipeOptional.get();
        assertThat(recipe.getId()).isPositive();
        assertThat(recipe.getFile())
                .isNotNull()
                .extracting(BobFile::getOriginalFileName).isEqualTo("test.png");

        // 재료 검증
        assertThat(recipe.getRecipeIngredients())
                .isNotEmpty()
                .hasSize(ingredientList.size())
                .extracting(recipeIngredient -> recipeIngredient.getIngredient().getId())
                .containsExactlyElementsOf(ingredientList.stream().map(Ingredient::getId).toList());

        // 디테일 검증
        List<RecipeDetail> recipeDetails = recipe.getRecipeDetails();
        assertThat(recipeDetails).isNotEmpty();
        for (int order = 1; order <= recipeDetails.size(); order++) {
            RecipeDetail recipeDetail = recipeDetails.get(order - 1);
            assertThat(recipeDetail.getId()).isPositive();
            assertThat(recipeDetail.getRecipeOrder()).isEqualTo(order);
            assertThat(recipeDetail.getRecipeDetailText()).isEqualTo(String.format("%d순서 입니다.", order));

            BobFile file = recipeDetail.getFile();
            assertThat(file)
                    .isNotNull()
                    .extracting(BobFile::getOriginalFileName).isEqualTo("test%d.png".formatted(order));
        }
    }

    private RecipeCreateDto createRecipeData() {
        RecipeCreateDto dto = new RecipeCreateDto();
        dto.setRecipeName("레시피");
        dto.setDifficulty(Difficulty.ANYONE);
        dto.setRecipeDescription("레시피 설명입니다요.");
        dto.setCookingTime((short) 30);

        // 재료 준비
        List<RecipeIngredientCreateDto> recipeIngredientCreateDtos = ingredientList
                .stream()
                .map(ingredient -> {
                    RecipeIngredientCreateDto ingredientCreateDto = new RecipeIngredientCreateDto();
                    ingredientCreateDto.setIngredientId(ingredient.getId());
                    ingredientCreateDto.setAmount("적당히");
                    return ingredientCreateDto;
                }).toList();
        dto.setIngredients(recipeIngredientCreateDtos);

        // 레시피 순서 준비
        List<RecipeDetailCreateDto> details = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            RecipeDetailCreateDto detailDto = new RecipeDetailCreateDto();
            detailDto.setRecipeDetailText(String.format("%d순서 입니다.", i));
            details.add(detailDto);
        }
        dto.setRecipeDetails(details);

        return dto;
    }

}