package com.my.bob.core.domain.recipe.converter;

import com.my.bob.core.domain.recipe.dto.response.*;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeDetail;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.my.bob.core.domain.file.converter.BobFileConverter.convertFileUrl;
import static com.my.bob.core.util.DateConvertUtil.convertDateToString;

public class RecipeConverter {

    private RecipeConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static RecipeListItemDto convertListItemDto(Recipe recipe) {
        // 재료 리스트 변환
        List<IngredientDto> ingredientList = recipe.getRecipeIngredients().stream()
                .map(RecipeIngredients::getIngredient)
                .distinct()
                .map(RecipeConverter::convertIngredientDto)
                .sorted(Comparator.comparing(IngredientDto::getIngredientName))
                .toList();

        return RecipeListItemDto.builder()
                .recipeId(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .recipeDescription(recipe.getRecipeDescription())
                .difficulty(recipe.getDifficulty())
                .imageUrl(recipe.getFile() == null ? null : recipe.getFile().getFileUrl())
                .ingredients(ingredientList)
                .build();
    }

    public static RecipeDto convertDto(Recipe recipe) {
        // 재료 번환
        List<RecipeIngredientDto> recipeIngredientDtoList = recipe.getRecipeIngredients()
                .stream()
                .map(RecipeConverter::convertRecipeIngredientDto)
                .toList();

        // detail 변환
        List<RecipeDetailDto> recipeDetailDtoList = recipe.getRecipeDetails()
                .stream()
                .map(RecipeConverter::convertDetailDto)
                .sorted(Comparator.comparingInt(RecipeDetailDto::getRecipeOrder))
                .toList();

        return RecipeDto.builder()
                .recipeId(recipe.getId())
                .recipeName(recipe.getRecipeName())
                .recipeDescription(recipe.getRecipeDescription())
                .difficulty(recipe.getDifficulty())
                .cookingTime(recipe.getCookingTime())
                .servings(recipe.getServings())
                .source(recipe.getSource())
                .recipeFileUrl(convertFileUrl(recipe.getFile()))

                .ingredients(recipeIngredientDtoList)
                .details(recipeDetailDtoList)

                .regId(recipe.getRegId())
                .regDate(recipeDateConvert(recipe.getRegDate()))
                .modId(recipe.getModId())
                .modDate(recipeDateConvert(recipe.getModDate()))
                .build();
    }

    public static IngredientDto convertIngredientDto(Ingredient ingredient) {
        return IngredientDto.builder()
                .id(ingredient.getId())
                .ingredientName(ingredient.getIngredientName())
                .ingredientDescription(ingredient.getIngredientDescription())
                .imageUrl(convertFileUrl(ingredient.getFile()))
                .build();
    }

    private static RecipeIngredientDto convertRecipeIngredientDto(RecipeIngredients recipeIngredient) {
        return RecipeIngredientDto.builder()
                .recipeIngredientId(recipeIngredient.getId())
                .ingredientId(recipeIngredient.getIngredient().getId())
                .ingredientDetailName(recipeIngredient.getIngredientDetailName())
                .amount(recipeIngredient.getAmount())

                .regId(recipeIngredient.getRegId())
                .regDate(recipeDateConvert(recipeIngredient.getRegDate()))
                .modId(recipeIngredient.getModId())
                .modDate(recipeDateConvert(recipeIngredient.getModDate()))
                .build();
    }

    private static RecipeDetailDto convertDetailDto(RecipeDetail recipeDetail) {
        return RecipeDetailDto.builder()
                .recipeDetailId(recipeDetail.getId())
                .recipeOrder(recipeDetail.getRecipeOrder())
                .recipeDetailText(recipeDetail.getRecipeDetailText())
                .recipeDetailFileUrl(convertFileUrl(recipeDetail.getFile()))

                .regDate(recipeDateConvert(recipeDetail.getRegDate()))
                .modDate(recipeDateConvert(recipeDetail.getModDate()))
                .build();
    }

    // 해당 converter 클래스에서는 yyyy-MM-dd 형태로 변환 되어 나간다
    private static String recipeDateConvert(LocalDateTime date) {
        return convertDateToString(date, "yyyy-MM-dd");
    }

}
