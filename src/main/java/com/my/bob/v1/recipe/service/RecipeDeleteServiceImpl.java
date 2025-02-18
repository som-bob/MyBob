package com.my.bob.v1.recipe.service;

import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.service.RecipeDeleteService;
import com.my.bob.core.domain.recipe.service.RecipeServiceHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecipeDeleteServiceImpl implements RecipeDeleteService {

    private final RecipeServiceHelper recipeServiceHelper;

    @Override
    @Transactional
    public void delete(Integer id) {
        Recipe recipe = recipeServiceHelper.getRecipe(id);

        recipe.delete();
    }
}
