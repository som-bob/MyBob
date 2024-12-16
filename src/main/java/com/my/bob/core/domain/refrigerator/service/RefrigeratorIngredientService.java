package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.refrigerator.dto.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.RefrigeratorDto;

public interface RefrigeratorIngredientService {

    RefrigeratorDto addIngredient(int refrigeratorId, RefrigeratorAddIngredientDto dto);
    RefrigeratorDto deleteIngredient(int refrigeratorId, int ingredientId);
    RefrigeratorDto deleteAllIngredients(int refrigeratorId);
}
