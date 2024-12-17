package com.my.bob.core.domain.recipe.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RecipeServiceHelper {

    private final IngredientRepository ingredientRepository;

    public Ingredient getIngredient(int ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
        if (optionalIngredient.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_INGREDIENT);
        }

        return optionalIngredient.get();
    }
}
