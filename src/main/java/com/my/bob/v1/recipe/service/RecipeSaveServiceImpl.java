package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.file.service.BobFileService;
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
import com.my.bob.core.external.s3.dto.response.FileSaveResponseDto;
import com.my.bob.core.external.s3.service.S3Service;
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

    private final BobFileService bobFileService;
    private final S3Service s3Service;

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeIngredientsRepository recipeIngredientsRepository;
    private final RecipeDetailRepository recipeDetailRepository;

    @Override
    @Transactional
    public int newRecipe(RecipeCreateDto dto) {
        Recipe savedRecipe = saveRecipe(dto);       // 레시피 저장
        precessRecipeIngredients(savedRecipe, dto.getIngredients());    // 재료 저장
        processRecipeDetails(savedRecipe, dto.getRecipeDetails());      // 레시피 디테일 상세 저장
        return savedRecipe.getId();
    }

    private Recipe saveRecipe(RecipeCreateDto dto) {
        Recipe recipe = Recipe.builder()
                .recipeName(dto.getRecipeName())
                .recipeDescription(dto.getRecipeDescription())
                .cookingTime(dto.getCookingTime())
                .difficulty(dto.getDifficulty())
                .servings(dto.getServings())
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        // 레시피 파일 저장
        MultipartFile recipeFile = dto.getRecipeFile();
        if (recipeFile != null) {
            BobFile bobRecipeFile = uploadAndSaveFile(recipeFile);
            savedRecipe.setRecipeFile(bobRecipeFile);
        }

        return recipe;
    }

    private void processRecipeDetails(Recipe recipe, List<RecipeDetailCreateDto> recipeDetails) {
        if (Collections.isEmpty(recipeDetails)) {
            return;
        }

        List<RecipeDetail> recipeDetailsList = IntStream.range(0, recipeDetails.size())
                .mapToObj(index -> {
                    RecipeDetailCreateDto detailDto = recipeDetails.get(index);

                    RecipeDetail recipeDetail = new RecipeDetail(recipe, index + 1, detailDto.getRecipeDetailText());
                    recipeDetailRepository.save(recipeDetail);

                    // 디테일 파일 저장
                    MultipartFile recipeDetailFile = detailDto.getRecipeDetailFile();
                    if (recipeDetailFile != null) {
                        BobFile bobRecipeFile = uploadAndSaveFile(recipeDetailFile);
                        recipeDetail.setRecipeDetailFile(bobRecipeFile);
                    }

                    return recipeDetail;
                }).toList();

        recipeDetailRepository.saveAll(recipeDetailsList);
    }

    private void precessRecipeIngredients(Recipe recipe, List<RecipeIngredientCreateDto> recipeIngredients) {
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
                .map(dto -> new RecipeIngredients(recipe, ingredientMap.get(dto.getIngredientId()), dto.getAmount()))
                .toList();

        recipeIngredientsRepository.saveAll(recipeIngredientsList);
    }

    private BobFile uploadAndSaveFile(MultipartFile file) {
        FileSaveResponseDto recipeFileSave = s3Service.uploadFile(file);
        return bobFileService.newFile(recipeFileSave.getFileUrl(),
                recipeFileSave.getOriginalFilename(),
                recipeFileSave.getFileName(),
                recipeFileSave.getFileSize(),
                recipeFileSave.getContentType());
    }
}
