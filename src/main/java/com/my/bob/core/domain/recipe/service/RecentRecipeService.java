package com.my.bob.core.domain.recipe.service;

public interface RecentRecipeService {
    static final int MAX_RECIPES = 10;

    void saveRecentRecipe(int id, String email, String recipeName, String recipeJson);
}
