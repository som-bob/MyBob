package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.dto.IngredientDto;

import java.util.List;

public interface IngredientService {

    List<IngredientDto> getAllIngredients();
}
