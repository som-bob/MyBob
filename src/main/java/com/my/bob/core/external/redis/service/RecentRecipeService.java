package com.my.bob.core.external.redis.service;

import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.external.redis.dto.RecentRecipe;

import java.util.List;
import java.util.Optional;

public interface RecentRecipeService {
    static final int MAX_RECIPES = 10;

    void saveRecentRecipe(String email, RecipeDto recipe);

    Optional<RecipeDto> getRecentRecipe(String email, String id);

    List<RecipeDto> getByEmail(String email);

    void clearRecipe(String email);
}
