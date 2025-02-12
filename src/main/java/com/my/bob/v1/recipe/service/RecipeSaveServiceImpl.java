package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.service.FileSaveService;
import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;
import com.my.bob.core.domain.recipe.dto.request.RecipeDetailCreateDto;
import com.my.bob.core.domain.recipe.dto.request.RecipeIngredientCreateDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeDetail;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeDetailRepository;
import com.my.bob.core.domain.recipe.repository.RecipeIngredientsRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.core.domain.recipe.service.RecipeSaveService;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeSaveServiceImpl implements RecipeSaveService {

    private final FileSaveService fileSaveService;

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final RecipeDetailRepository recipeDetailRepository;

    @Override
    @Transactional
    public int newRecipe(RecipeCreateDto dto, MultipartFile recipeFile, MultipartFile[] recipeDetailsFiles) {
        mapFilesToDto(dto, recipeFile, recipeDetailsFiles);   // 파일 세팅

        Recipe savedRecipe = saveRecipe(dto);       // 레시피 저장
        processRecipeIngredients(savedRecipe, dto.getIngredients());    // 재료 저장
        processRecipeDetails(savedRecipe, dto.getRecipeDetails());      // 레시피 디테일 상세 저장
        return savedRecipe.getId();
    }

    private void mapFilesToDto(RecipeCreateDto dto,
                               MultipartFile recipeFile, MultipartFile[] recipeDetailsFiles) {
        dto.setRecipeFile(recipeFile);

        if (Collections.isEmpty(dto.getRecipeDetails()) || recipeDetailsFiles == null) {
            return;
        }

        List<RecipeDetailCreateDto> recipeDetails = dto.getRecipeDetails();
        if (!Collections.isEmpty(recipeDetails)) {
            for (int index = 0; index < recipeDetailsFiles.length; index++) {
                MultipartFile recipeDetailsFile = recipeDetailsFiles[index];
                if(! recipeDetailsFile.isEmpty()){
                    recipeDetails.get(index).setRecipeDetailFile(recipeDetailsFile);
                }
            }
        }
    }

    private Recipe saveRecipe(RecipeCreateDto dto) {
        Recipe recipe = Recipe.builder()
                .recipeName(dto.getRecipeName())
                .recipeDescription(dto.getRecipeDescription())
                .cookingTime(dto.getCookingTime())
                .difficulty(dto.getDifficulty())
                .servings(dto.getServings())
                .build();

        // 레시피 파일 저장
        MultipartFile recipeFile = dto.getRecipeFile();
        if (recipeFile != null) {
            BobFile bobRecipeFile = fileSaveService.uploadAndSaveFile(recipeFile);
            recipe.setRecipeFile(bobRecipeFile);
        }

        return recipeRepository.save(recipe);
    }

    private void processRecipeDetails(Recipe recipe, List<RecipeDetailCreateDto> recipeDetails) {
        if (Collections.isEmpty(recipeDetails)) {
            return;
        }

        List<RecipeDetail> recipeDetailsList = IntStream.range(0, recipeDetails.size())
                .mapToObj(index -> {
                    RecipeDetailCreateDto detailDto = recipeDetails.get(index);

                    RecipeDetail recipeDetail = new RecipeDetail(recipe, index + 1, detailDto.getRecipeDetailText());

                    // 디테일 파일 저장
                    MultipartFile recipeDetailFile = detailDto.getRecipeDetailFile();
                    if (recipeDetailFile != null) {
                        BobFile bobRecipeFile = fileSaveService.uploadAndSaveFile(recipeDetailFile);
                        recipeDetail.setRecipeDetailFile(bobRecipeFile);
                    }

                    return recipeDetail;
                }).toList();

        recipeDetailRepository.saveAll(recipeDetailsList);
    }

    private void processRecipeIngredients(Recipe recipe, List<RecipeIngredientCreateDto> recipeIngredients) {
        if (Collections.isEmpty(recipeIngredients)) {
            return;
        }

        Set<Integer> ingredientIds = recipeIngredients.stream()
                .map(RecipeIngredientCreateDto::getIngredientId)
                .collect(Collectors.toSet());

        Map<Integer, Ingredient> ingredientMap = ingredientRepository.findByIdIn(ingredientIds)
                .stream()
                .collect(Collectors.toMap(Ingredient::getId, Function.identity()));


        List<RecipeIngredients> recipeIngredientsList = recipeIngredients.stream()
                .map(dto ->
                        new RecipeIngredients(recipe, ingredientMap.get(dto.getIngredientId()),
                                dto.getIngredientDetailName(), dto.getAmount()))
                .toList();

        recipeIngredientsRepository.saveAll(recipeIngredientsList);
    }

}
