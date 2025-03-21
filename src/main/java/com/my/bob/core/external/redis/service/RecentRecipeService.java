package com.my.bob.core.external.redis.service;

import com.my.bob.core.domain.recipe.dto.response.RecipeDto;

import java.util.List;

public interface RecentRecipeService {
    int MAX_RECIPES = 10;

    void saveRecentRecipe(String email, RecipeDto recipe);

    List<RecipeDto> getRecentRecipes(String email);

    void clearRecipe(String email);
}
