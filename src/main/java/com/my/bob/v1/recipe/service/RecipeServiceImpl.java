package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.recipe.dto.request.RecipeSearchDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.repository.RecipeQueryRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import com.my.bob.core.domain.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RecipeQueryRepository recipeQueryRepository;


    @Override
    public Page<RecipeListItemDto> getRecipes(Pageable pageable, RecipeSearchDto dto) {
        return recipeQueryRepository.getByParam(pageable, dto);
    }
}
