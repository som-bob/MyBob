package com.my.bob.core.external.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecentRecipe {
    private String id;

    private String recipeName;
    private String recipeJson;
}
