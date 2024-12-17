package com.my.bob.core.domain.recipe.exception;

import com.my.bob.core.constants.ErrorMessage;

public class IngredientNotFoundException extends IngredientException {
    public IngredientNotFoundException() {
        super(ErrorMessage.NOT_EXISTENT_INGREDIENT);
    }
}
