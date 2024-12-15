package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;

public interface RefrigeratorIngredientService {

    RefrigeratorDto addIngredient(int refrigeratorId, int ingredientId);
    RefrigeratorDto deleteIngredient(int refrigeratorId, int ingredientId);
}
