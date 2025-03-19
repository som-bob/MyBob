package com.my.bob.core.domain.recipe.service;

import com.my.bob.account.WithAccount;
import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.repsitory.BobFileRepository;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.dto.request.*;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeDetail;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeDetailRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.my.bob.core.domain.recipe.service.helper.RecipeSaveHelper.saveIngredient;
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
    RecipeDetailRepository recipeDetailRepository;

    @Autowired
    RecipeIngredientsRepository recipeIngredientsRepository;

    @Autowired
    BobFileRepository bobFileRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    private List<Ingredient> ingredientList;
    private List<Ingredient> updateIngredientList;

    @BeforeEach
    void setUp() {
        // 기본 재료 저장
        Ingredient ingredient1 = saveIngredient(ingredientRepository, "가_테스트 재료");
        Ingredient ingredient2 = saveIngredient(ingredientRepository, "나_테스트 재료");
        Ingredient ingredient3 = saveIngredient(ingredientRepository, "다_테스트 재료");

        ingredientList = List.of(ingredient1, ingredient2, ingredient3);
        updateIngredientList = List.of(ingredient1, ingredient2);
    }

    @AfterEach
    void tearDown() {
        recipeDetailRepository.deleteAllInBatch();
        recipeIngredientsRepository.deleteAllInBatch();
        recipeRepository.deleteAllInBatch();
        bobFileRepository.deleteAllInBatch();
        ingredientRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("레시피 저장 테스트")
    @Transactional
    void saveRecipe() throws IOException {
        // given
        RecipeCreateDto dto = createRecipeData();
        MultipartFile recipeFile = getFileFromResource("test.png");
        List<MultipartFile> recipeDetailsFilesList = new ArrayList<>();
        for (int i = 1; i <= dto.getRecipeDetails().size(); i++) {
            MultipartFile recipeDetail = getFileFromResource(String.format("test%d.png", i));
            recipeDetailsFilesList.add(recipeDetail);
        }
        MultipartFile[] recipeDetailsFiles = recipeDetailsFilesList.toArray(new MultipartFile[0]);

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
        assertThat(recipe.getRecipeName()).contains("레시피");
        assertThat(recipe.getRecipeDescription()).contains("레시피");

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


    @Test
    @Transactional
    @DisplayName("레시피 업데이트 테스트")
    void updateRecipe() throws IOException {
        // given
        // 기존 저장 데이터
        int recipeId = saveRecipeData();
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        assertThat(recipeOptional).isPresent();
        // 업데이트 데이터
        RecipeUpdateDto dto = updateRecipeData(recipeOptional.get());
        MultipartFile updateRecipeFile = getFileFromResource("updateTest.png");
        // 파일들
        List<MultipartFile> recipeDetailsFilesList = new ArrayList<>();
        List<RecipeDetailUpdateDto> details = dto.getDetails();
        for (int i = 1; i <= details.size(); i++) {
            MultipartFile recipeDetail = getFileFromResource(String.format("updateTest%d.png", i));
            recipeDetailsFilesList.add(recipeDetail);
        }
        MultipartFile[] recipeDetailsFiles = recipeDetailsFilesList.toArray(new MultipartFile[0]);
        // 첫번째 사진은 없는 것으로 업데이트 (사진 비우기)
        recipeDetailsFiles[0] = new MockMultipartFile("emptyFile", new byte[0]); // 빈 파일

        // when
        recipeSaveService.updateRecipe(recipeId, dto, updateRecipeFile, recipeDetailsFiles);

        // then
        Optional<Recipe> refindRecipeOptional = recipeRepository.findById(recipeId);
        assertThat(refindRecipeOptional).isPresent();
        Recipe refindRecipe = refindRecipeOptional.get();
        assertThat(refindRecipe.getFile())
                .isNotNull()
                .extracting(BobFile::getOriginalFileName).isEqualTo("updateTest.png");
        assertThat(refindRecipe.getRecipeName()).contains("업데이트");
        assertThat(refindRecipe.getRecipeDescription()).contains("업데이트");

        // 재료 검증
        assertThat(refindRecipe.getRecipeIngredients())
                .isNotEmpty()
                .hasSize(updateIngredientList.size())
                .extracting(recipeIngredient -> recipeIngredient.getIngredient().getId())
                .containsExactlyElementsOf(updateIngredientList.stream().map(Ingredient::getId).toList());

        // 디테일 검증
        List<RecipeDetail> recipeDetails = refindRecipe.getRecipeDetails();
        assertThat(recipeDetails).isNotEmpty();
        for (int order = 1; order <= recipeDetails.size(); order++) {
            RecipeDetailUpdateDto detailUpdateDto = details.get(order - 1);
            RecipeDetail recipeDetail = recipeDetails.get(order - 1);

            assertThat(recipeDetail.getId()).isPositive();
            assertThat(recipeDetail.getRecipeOrder()).isEqualTo(order);
            assertThat(recipeDetail.getRecipeDetailText()).isEqualTo(detailUpdateDto.getRecipeDetailText());

            boolean fileChange = detailUpdateDto.isFileChange();
            if (fileChange) {
                MultipartFile recipeDetailsFile = recipeDetailsFiles[order - 1];
                if (recipeDetailsFile.isEmpty()) {
                    // 파일이 비었는지도 검증
                    assertThat(recipeDetail.getFile()).isNull();
                } else {

                    BobFile file = recipeDetail.getFile();
                    if (file != null) {
                        assertThat(file)
                                .isNotNull()
                                .extracting(BobFile::getOriginalFileName).isEqualTo("updateTest%d.png".formatted(order));
                    }
                }
            }
        }
    }


    /* private method */
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

    private RecipeUpdateDto updateRecipeData(Recipe recipe) {
        RecipeUpdateDto dto = new RecipeUpdateDto();
        dto.setRecipeName("업데이트 레시피");
        dto.setDifficulty(Difficulty.MIDRANGE);
        dto.setRecipeDescription("업데이트 레시피 설명입니다요.");
        dto.setCookingTime((short) 60);
        dto.setFileChange(true);    // 파일 변경

        // 재료 준비 (기존 재료와 동일)
        List<RecipeIngredientCreateDto> recipeIngredientCreateDtos = updateIngredientList
                .stream()
                .map(ingredient -> {
                    RecipeIngredientCreateDto ingredientCreateDto = new RecipeIngredientCreateDto();
                    ingredientCreateDto.setIngredientId(ingredient.getId());
                    ingredientCreateDto.setAmount("업데이트 적당히");
                    return ingredientCreateDto;
                }).toList();
        dto.setIngredients(recipeIngredientCreateDtos);

        // 레시피 순서 준비
        // 모든 순서 업데이트 (사진들도)
        List<RecipeDetailUpdateDto> details = new ArrayList<>();
        dto.setDetails(details);

        List<RecipeDetail> recipeDetails = recipe.getRecipeDetails();
        int detailSize = recipeDetails.size();
        for (int num = 1; num <= detailSize; num++) {
            RecipeDetail recipeDetail = recipeDetails.get(num - 1);
            details.add(getRecipeDetailUpdate(recipeDetail.getId(), num, true));
        }

        // 새로운 순서 두개 추가
        details.add(getRecipeDetailUpdate(null, detailSize + 1, true));
        details.add(getRecipeDetailUpdate(null, detailSize + 2, false));

        return dto;
    }

    private static RecipeDetailUpdateDto getRecipeDetailUpdate(Integer recipeDetailId,
                                                               int order, boolean isFileChange) {
        RecipeDetailUpdateDto detailUpdateDto = new RecipeDetailUpdateDto();
        detailUpdateDto.setRecipeDetailId(recipeDetailId);
        detailUpdateDto.setFileChange(isFileChange);
        detailUpdateDto.setOrder(order);
        detailUpdateDto.setRecipeDetailText(String.format("업데이트 %d순서 입니다.", order));
        return detailUpdateDto;
    }

    private int saveRecipeData() throws IOException {
        RecipeCreateDto dto = createRecipeData();
        MultipartFile recipeFile = getFileFromResource("test.png");
        List<MultipartFile> recipeDetailsFilesList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            MultipartFile recipeDetail = getFileFromResource(String.format("test%d.png", i));
            recipeDetailsFilesList.add(recipeDetail);
        }
        MultipartFile[] recipeDetailsFiles = recipeDetailsFilesList.toArray(new MultipartFile[0]);

        return recipeSaveService.newRecipe(dto, recipeFile, recipeDetailsFiles);
    }

}