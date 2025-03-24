package com.my.bob.core.external.redis.dto;

import com.my.bob.core.domain.recipe.dto.response.RecipeDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecentRecipeViewedEvent {

    private String email;
    private RecipeDto recipe;
}
