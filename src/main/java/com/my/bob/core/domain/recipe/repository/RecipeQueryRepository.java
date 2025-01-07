package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.dto.RecipeListItemDto;
import com.my.bob.core.domain.recipe.dto.RecipeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeQueryRepository {

    Page<RecipeListItemDto> getByParam(Pageable pageable, RecipeSearchDto dto);
}
