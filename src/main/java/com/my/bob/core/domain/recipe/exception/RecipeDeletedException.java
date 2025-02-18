package com.my.bob.core.domain.recipe.exception;

import com.my.bob.core.constants.ErrorMessage;

public class RecipeDeletedException extends RecipeException{

    public RecipeDeletedException() {
        super(ErrorMessage.DELETED_RECIPE);
    }
}
