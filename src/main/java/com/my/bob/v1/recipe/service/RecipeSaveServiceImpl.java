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
    public long newRecipe(RecipeCreateDto dto) {
        // 레시피 저장
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
        if(recipeFile != null) {
            BobFile bobRecipeFile = getFileAfterSave(recipeFile);
            savedRecipe.setRecipeFile(bobRecipeFile);
        }

        // 예외처리 추가 필요 (재료 없을 경우 exception 등등..)
        // 재료 저장
        List<RecipeIngredientCreateDto> recipeIngredients = dto.getIngredients();
        saveRecipeIngredients(recipe, recipeIngredients);

        // 레시피 디테일 상세 저장
        List<RecipeDetailCreateDto> recipeDetails = dto.getRecipeDetails();
        saveRecipeDetails(recipe, recipeDetails);

        return savedRecipe.getId();
    }

    private void saveRecipeDetails(Recipe recipe, List<RecipeDetailCreateDto> recipeDetails) {
        if(Collections.isEmpty(recipeDetails)) {
            return;
        }

        for (int order = 1; order <= recipeDetails.size(); order++) {
            RecipeDetailCreateDto detailDto = recipeDetails.get(order - 1);

            RecipeDetail recipeDetail = new RecipeDetail(recipe, order, detailDto.getRecipeDetailText());
            recipeDetailRepository.save(recipeDetail);

            // 디테일 파일 저장
            MultipartFile recipeDetailFile = detailDto.getRecipeDetailFile();
            if(recipeDetailFile != null) {
                BobFile bobRecipeFile = getFileAfterSave(recipeDetailFile);
                recipeDetail.setRecipeDetailFile(bobRecipeFile);
            }
        }
    }

    private void saveRecipeIngredients(Recipe recipe, List<RecipeIngredientCreateDto> recipeIngredients) {
        if(Collections.isEmpty(recipeIngredients)) {
            return;
        }

        Set<Integer> ingredientIds = recipeIngredients.stream()
                .map(RecipeIngredientCreateDto::getIngredientId)
                .collect(Collectors.toSet());

        List<Ingredient> findIngredients = ingredientRepository.findByIdIn(ingredientIds);
        Map<Integer, Ingredient> ingredientMap = findIngredients
                .stream()
                .collect(Collectors.toMap(Ingredient::getId, Function.identity()));

        for (RecipeIngredientCreateDto recipeIngredientDto : recipeIngredients) {
            Integer ingredientId = recipeIngredientDto.getIngredientId();
            Ingredient ingredient = ingredientMap.get(ingredientId);

            RecipeIngredients recipeIngredient =
                    new RecipeIngredients(recipe, ingredient, recipeIngredientDto.getAmount());
            recipeIngredientsRepository.save(recipeIngredient);
        }
    }

    private BobFile getFileAfterSave(MultipartFile file) {
        FileSaveResponseDto recipeFileSave = s3Service.uploadFile(file);
        return bobFileService.newFile(recipeFileSave.getFileUrl(),
                recipeFileSave.getOriginalFilename(),
                recipeFileSave.getFileName(),
                recipeFileSave.getFileSize(),
                recipeFileSave.getContentType());
    }



}
