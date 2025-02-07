package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.entity.Recipe;
import com.my.bob.core.domain.recipe.exception.IngredientNotFoundException;
import com.my.bob.core.domain.recipe.exception.RecipeNotFoundException;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecipeServiceHelper {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public Ingredient getIngredient(int ingredientId) {
        Optional<Ingredient> ingredientOptional = ingredientRepository.findById(ingredientId);
        if (ingredientOptional.isEmpty()) {
            throw new IngredientNotFoundException();
        }

        return ingredientOptional.get();
    }

    public Recipe getRecipe(int recipeId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);
        if(recipeOptional.isEmpty()) {
            throw new RecipeNotFoundException();
        }

        return recipeOptional.get();
    }
}
