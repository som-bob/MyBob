package com.my.bob.core.domain.recipe.converter;

import com.my.bob.core.domain.recipe.dto.response.IngredientDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;

import java.util.Comparator;
import java.util.List;

public class RecipeConverter {

    private RecipeConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static RecipeListItemDto convertDto(Recipe recipe) {
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
                .imageUrl(recipe.getFile() == null? null : recipe.getFile().getFileUrl())
                .ingredients(ingredientList)
                .build();
    }

    public static IngredientDto convertDto(Ingredient ingredient) {
        return convertIngredientDto(ingredient);
    }

    private static IngredientDto convertIngredientDto(Ingredient ingredient) {
        return IngredientDto.builder()
                .id(ingredient.getId())
                .ingredientName(ingredient.getIngredientName())
                .ingredientDescription(ingredient.getIngredientDescription())
                .imageUrl((ingredient.getFile() != null) ? ingredient.getFile().getFileUrl() : null)
                .build();
    }
}
