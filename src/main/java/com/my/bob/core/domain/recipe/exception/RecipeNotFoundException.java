package com.my.bob.core.domain.recipe.exception;

import com.my.bob.core.constants.ErrorMessage;

public class RecipeNotFoundException extends RecipeException {
    public RecipeNotFoundException() {
        super(ErrorMessage.NOT_EXISTENT_RECIPE);
    }
}
