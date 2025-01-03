package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.dto.RecipeListItemDto;
import com.my.bob.core.domain.recipe.dto.RecipeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {

    Page<RecipeListItemDto> getRecipes(Pageable pageable, RecipeSearchDto dto);
}
