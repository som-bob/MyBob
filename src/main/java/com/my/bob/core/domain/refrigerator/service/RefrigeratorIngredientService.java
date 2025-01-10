package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;

public interface RefrigeratorIngredientService {

    RefrigeratorDto addIngredient(int refrigeratorId, RefrigeratorAddIngredientDto dto);
    RefrigeratorDto deleteIngredient(int refrigeratorId, int refrigeratorIngredientId);
    RefrigeratorDto deleteAllIngredients(int refrigeratorId);
}
