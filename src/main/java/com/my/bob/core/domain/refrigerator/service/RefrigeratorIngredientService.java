package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.domain.refrigerator.dto.request.RefrigeratorAddIngredientDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorDto;
import com.my.bob.core.domain.refrigerator.dto.response.RefrigeratorInIngredientDto;

import java.util.List;

public interface RefrigeratorIngredientService {

    RefrigeratorDto addIngredient(int refrigeratorId, RefrigeratorAddIngredientDto dto);
    RefrigeratorDto deleteIngredient(int refrigeratorId, int refrigeratorIngredientId);
    RefrigeratorDto deleteAllIngredients(int refrigeratorId);

    // 로그인 한 계정의 냉장고에 들어 있는 재료 리스트
    List<RefrigeratorInIngredientDto> getAllIngredients(String email);
}
