package com.my.bob.core.domain.recipe.dto.response;

import lombok.Data;

@Data
public class RecipeDetailDto {

    private long recipeDetailId;
    private String recipeDetailFileUrl;
    private String recipeDetailText;

    private String regDate;
    private String regId;
    private String modDate;
    private String modId;
}
