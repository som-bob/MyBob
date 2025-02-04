package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.dto.request.RecipeCreateDto;

public interface RecipeSaveService {
    long newRecipe(RecipeCreateDto dto);
}
