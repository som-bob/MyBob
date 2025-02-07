package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.recipe.dto.request.RecipeSearchDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import com.my.bob.core.domain.recipe.dto.response.RecipeListItemDto;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.repository.RecipeQueryRepository;
import com.my.bob.core.domain.recipe.service.RecipeService;
import com.my.bob.core.domain.recipe.service.RecipeServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.my.bob.core.domain.recipe.converter.RecipeConverter.convertDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {

    private final RecipeQueryRepository recipeQueryRepository;

    private final RecipeServiceHelper recipeServiceHelper;

    @Override
    public Page<RecipeListItemDto> getRecipes(Pageable pageable, RecipeSearchDto dto) {
        return recipeQueryRepository.getByParam(pageable, dto);
    }

    @Override
    public RecipeDto getRecipe(Integer id) {
        Recipe recipe = recipeServiceHelper.getRecipe(id);

        return convertDto(recipe);
    }
}
