package com.my.bob.core.domain.recipe.dto.response;

import com.my.bob.core.domain.file.entity.BobFile;
import com.my.bob.core.domain.recipe.contants.Difficulty;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.entity.RecipeIngredients;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeListItemDto {
    // 목록 조회용 Dto

    private int recipeId;
    private String recipeName;
    private String recipeDescription;
    private String imageUrl;

    private String servings;
    private Difficulty difficulty;
    private List<IngredientDto> ingredients;

    public RecipeListItemDto(Recipe recipe) {
        this.recipeId = recipe.getId();
        this.recipeName = recipe.getRecipeName();
        this.recipeDescription = recipe.getRecipeDescription();

        this.imageUrl = Optional.ofNullable(recipe.getFile())
                .map(BobFile::getFileUrl)
                .orElse(null);
        this.servings = recipe.getServings();
        this.difficulty = recipe.getDifficulty();

        List<Ingredient> ingredientList = recipe.getRecipeIngredients()
                .stream()
                .map(RecipeIngredients::getIngredient)
                .collect(Collectors.toMap(
                        Ingredient::getId,      // 중복 제거를 위한 key (id)
                        ingredient -> ingredient,   // 실제 값
                        (existing, replacement) -> existing,    // 중복 발생시 기존 값 유지
                        LinkedHashMap::new  // 순서를 유지 하는 Map
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(Ingredient::getIngredientName))
                .toList();

        this.ingredients = ingredientList
                .stream()
                .map(IngredientDto::new)
                .toList();
    }
}
