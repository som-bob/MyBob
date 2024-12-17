package com.my.bob.core.domain.refrigerator.service;

import com.my.bob.core.constants.ErrorMessage;
import com.my.bob.core.domain.recipe.entity.Ingredient;
import com.my.bob.core.domain.recipe.repository.IngredientRepository;
import com.my.bob.core.domain.refrigerator.entity.Refrigerator;
import com.my.bob.core.domain.refrigerator.repository.RefrigeratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefrigeratorServiceHelper {

    private final RefrigeratorRepository refrigeratorRepository;
    private final IngredientRepository ingredientRepository;

    public Refrigerator getRefrigerator(int refrigeratorId) {
        Optional<Refrigerator> optionalRefrigerator = refrigeratorRepository.findById(refrigeratorId);
        if (optionalRefrigerator.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_REFRIGERATOR);
        }

        return optionalRefrigerator.get();
    }

    public Ingredient getIngredient(int ingredientId) {
        Optional<Ingredient> optionalIngredient = ingredientRepository.findById(ingredientId);
        if (optionalIngredient.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.NOT_EXISTENT_INGREDIENT);
        }

        return optionalIngredient.get();
    }
}
