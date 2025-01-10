package com.my.bob.core.domain.recipe.repository;

import com.my.bob.core.domain.recipe.dto.request.RecipeSearchDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeQueryRepository {

    Page<RecipeListItemDto> getByParam(Pageable pageable, RecipeSearchDto dto);
}
